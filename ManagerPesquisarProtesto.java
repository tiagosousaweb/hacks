package br.com.foxinline.manager.pesquisar;

import br.com.foxinline.annotations.AccessPermission;
import br.com.foxinline.enums.AplicacaoCampo;
import br.com.foxinline.enums.CategoriaProtocolo;
import br.com.foxinline.enums.Classe;
import br.com.foxinline.enums.Relatorio;
import br.com.foxinline.enums.RelatorioApontamento;
import br.com.foxinline.enums.SituacaoEnvioProtesto;
import br.com.foxinline.enums.SituacaoEnvioSelo;
import br.com.foxinline.enums.StatusProtesto;
import br.com.foxinline.enums.StatusProtocolo;
import br.com.foxinline.enums.TipoBaixaProtesto;
import br.com.foxinline.enums.TipoDeConsultaDeTitulos;
import br.com.foxinline.enums.TipoProtesto;
import br.com.foxinline.enums.TipoProtocolo;
import br.com.foxinline.enums.TipoRelatorioProtesto;
import br.com.foxinline.enums.TipoSituacaoProtesto;
import br.com.foxinline.enums.TipoSolucaoSuspensaoProtesto;
import br.com.foxinline.enums.TipoTelefone;
import br.com.foxinline.enums.boleto.StatusBoleto;
import br.com.foxinline.enums.boleto.StatusRemessa;
import br.com.foxinline.exception.CabecalhoException;
import br.com.foxinline.helper.ProtocoloItemHelper;
import br.com.foxinline.helper.SelagemHelper;
import br.com.foxinline.manager.BasicManagerSearch;
import br.com.foxinline.manager.criar.ManagerCriarProtocolo;
import br.com.foxinline.model.LogHistory;
import br.com.foxinline.model.UserSystem;
import br.com.foxinline.modelo.Anexo;
import br.com.foxinline.modelo.Ato;
import br.com.foxinline.modelo.AtoCampo;
import br.com.foxinline.modelo.AtoGenerico;
import br.com.foxinline.modelo.Boleto;
import br.com.foxinline.modelo.Cartorio;
import br.com.foxinline.modelo.ConfiguracaoSistema;
import br.com.foxinline.modelo.ContaBancaria;
import br.com.foxinline.modelo.ControlePagamentoProtesto;
import br.com.foxinline.modelo.CustaProtesto;
import br.com.foxinline.modelo.EmolumentoItem;
import br.com.foxinline.modelo.EspecieTitulo;
import br.com.foxinline.modelo.Imposto;
import br.com.foxinline.modelo.ImpostoProtocolo;
import br.com.foxinline.modelo.IrregularidadeProtesto;
import br.com.foxinline.modelo.ItemLivroProtocolo;
import br.com.foxinline.modelo.Livro;
import br.com.foxinline.modelo.ModeloProcesso;
import br.com.foxinline.modelo.MotivoEditalProtesto;
import br.com.foxinline.modelo.MotivoSuspensaoProtesto;
import br.com.foxinline.modelo.Ordenacao;
import br.com.foxinline.modelo.Pessoa;
import br.com.foxinline.modelo.Protesto;
import br.com.foxinline.modelo.Protocolo;
import br.com.foxinline.modelo.ProtocoloItem;
import br.com.foxinline.modelo.Selo;
import br.com.foxinline.modelo.SituacaoProtesto;
import br.com.foxinline.modelo.Telefone;
import br.com.foxinline.modelo.Usuario;
import br.com.foxinline.modelo.documento.DocumentoAnexo;
import br.com.foxinline.modelo.documento.ModeloDocumento;
import br.com.foxinline.modelo.email.Email;
import br.com.foxinline.protesto.exceptions.ProtestoException;
import br.com.foxinline.relatorio.util.RelatorioUtil;
import br.com.foxinline.servico.AnexoServico;
import br.com.foxinline.servico.AtoGenericoServico;
import br.com.foxinline.servico.AtoServico;
import br.com.foxinline.servico.BoletoServico;
import br.com.foxinline.servico.CartorioServico;
import br.com.foxinline.servico.ConfiguracaoImpressaoServico;
import br.com.foxinline.servico.ConfiguracaoSistemaServico;
import br.com.foxinline.servico.ControlePagamentoProtestoServico;
import br.com.foxinline.servico.EmolumentoItemServico;
import br.com.foxinline.servico.EspecieTituloServico;
import br.com.foxinline.servico.FeriadoServico;
import br.com.foxinline.servico.ImpostoServico;
import br.com.foxinline.servico.IrregularidadeProtestoServico;
import br.com.foxinline.servico.ItemLivroProtocoloServico;
import br.com.foxinline.servico.LivroServico;
import br.com.foxinline.servico.ModeloProcessoServico;
import br.com.foxinline.servico.PessoaServico;
import br.com.foxinline.servico.ProtestoServico;
import br.com.foxinline.servico.ProtocoloItemServico;
import br.com.foxinline.servico.ProtocoloServico;
import br.com.foxinline.servico.RegistroFinanceiroServico;
import br.com.foxinline.servico.RelatorioServico;
import br.com.foxinline.servico.SelarServico;
import br.com.foxinline.servico.SeloServico;
import br.com.foxinline.servico.SituacaoProtestoServico;
import br.com.foxinline.servico.TipoSeloServico;
import br.com.foxinline.servico.TramitacaoServico;
import br.com.foxinline.servico.TramiteServico;
import br.com.foxinline.servico.UsuarioServico;
import br.com.foxinline.servico.email.EmailServico;
import br.com.foxinline.servico.modelo.ModeloDocumentoServico;
import br.com.foxinline.servico.sms.SmsServico;
import br.com.foxinline.util.DateUtils;
import br.com.foxinline.util.UserUtils;
import br.com.foxinline.util.Utils;
import br.com.foxinline.utilitario.Caracteres;
import br.com.foxinline.utilitario.DocumentoAnexoUtils;
import br.com.foxinline.utilitario.Mensagem;
import br.com.foxinline.utilitario.ProtestoUtils;
import br.com.foxinline.utilitario.ProtocoloUtils;
import br.com.foxinline.utilitario.boleto.BoletoException;
import br.com.foxinline.vo.EnviarEmailVo;
import br.com.foxinline.vo.ImpostoVO;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author roniere
 */
@ManagedBean
@ViewScoped
@AccessPermission(moduleName = "PROTESTO", pageRedirect = "indexProtesto.xhtml")
public class ManagerPesquisarProtesto extends BasicManagerSearch implements Serializable {

    @EJB
    PessoaServico pessoaServico;
    @EJB
    ProtestoServico protestoServico;
    @EJB
    SituacaoProtestoServico situacaoProtestoServico;
    @EJB
    ProtocoloServico protocoloServico;
    @EJB
    UsuarioServico usuarioServico;
    @EJB
    CartorioServico cartorioServico;
    @EJB
    ConfiguracaoImpressaoServico configuracaoImpressaoServico;
    @EJB
    EspecieTituloServico especieTituloServico;
    @EJB
    AtoGenericoServico atoGenericoServico;
    @EJB
    FeriadoServico feriadoServico;
    @EJB
    AtoServico atoServico;
    @EJB
    SeloServico seloServico;
    @EJB
    EmolumentoItemServico emolumentoItemServico;
    @EJB
    LivroServico livroServico;
    @EJB
    ConfiguracaoSistemaServico configuracaoSistemaServico;
    @EJB
    TramiteServico tramiteServico;
    @EJB
    TramitacaoServico tramitacaoServico;
    @EJB
    ProtocoloItemServico protocoloItemServico;
    @EJB
    IrregularidadeProtestoServico irregularidadeProtestoServico;
    @EJB
    RegistroFinanceiroServico registroFinanceiroServico;
    @EJB
    BoletoServico boletoServico;
    @EJB
    SelarServico selarServico;
    @EJB
    ItemLivroProtocoloServico itemLivroProtocoloServico;
    @EJB
    ControlePagamentoProtestoServico controlePagamentoProtestoServico;
    @EJB
    AnexoServico anexoServico;
    @EJB
    ImpostoServico impostoServico;
    @EJB
    private ModeloDocumentoServico modeloDocumentoServico;
    @EJB
    private ModeloProcessoServico modeloProcessoServico;
    @EJB
    private TipoSeloServico tipoSeloServico;
    @EJB
    private SmsServico smsServico;
    @EJB
    private EmailServico emailServico;
    private List<TipoSituacaoProtesto> tipoSituacaoProtestoRelatorio;
    private EspecieTitulo especieTitulo;
    private Protocolo protocoloApontamentoTitulo;
    private Protocolo protocoloRetirada;
    private Protesto protestoPesquisa;
    private Protesto protestoDeletar;
    private Protesto protestoVisualizar;
    private Protesto protestoEditar;
    private Pessoa interessado;
    private List<Protesto> protestos;
    private List<Protesto> protestosFimFalimentar;
    private List<Protocolo> protocolos;
    private List<SituacaoProtesto> situacoesProtesto;
    private SituacaoProtesto spRemocao;
    private SituacaoProtesto spEditar;
    private Pessoa apresentante;
    private List<Pessoa> apresentantes;
    private List<Pessoa> devedores;
    private List<Pessoa> portadores;
    private List<Pessoa> fiadores;
    private Date dataInicio;
    private Date dataFim;
    private Date dataInicioApontamento;
    private Date dataFimApontamento;
    private Date dataAceite;
    private String arquivoId;
    private String protestoid;
    private Cartorio cartorio;
    private String codigoApontamento;
    private String codigosApontamentos;
    private List<Protesto> protestosEmitirIntimacao;
    private List<Protesto> protestosNotificados;
    private BigDecimal totalProcesso;
    private BigDecimal totalCustasProtesto;
    private Pessoa devedor;
    private Usuario usuarioAssinatura;
    private Pessoa devedorCorrecao;
    private long protocoloId;
    private TipoRelatorioProtesto tipoRelatorio;
    private Long tipoRelatorioProtestados;
    private Long tipoRelatorioBaixados;
    private boolean emitirInstrumentosAnexados = true;
    private Long processo;
    private Long numeroRemessa;
    private Long numeroUltimaRemessa;
    private Date dataPrazoMin;
    private Date dataPrazo;
    private Livro livroApontamento;
    private Livro livroProtesto;
    private Long numeroApontamentoInicial;
    private boolean viaLivro;
    private boolean adicionarCustas = false;
    private boolean editalValorFixo;
    private TipoBaixaProtesto tipoBaixaProtesto;
    private ConfiguracaoSistema configuracao;
    private IrregularidadeProtesto irregularidade;
    private TipoSituacaoProtesto tipoSituacaoProtesto;
    private List<IrregularidadeProtesto> irregularidades;
    private Date minDate;
    private BigDecimal valorEdital;
    private String tipoEmissaoCartas = "";
    private String materializarPor = "";
    private Integer titulosPesquisa;
    private Integer central = 1;
    private CustaProtesto custaProtesto;
    private CustaProtesto custaRemocao;
    private String tituloSerasa;
    private boolean selando;
    private boolean exibirDialogSelos;
    private List<Selo> selosExibicao;
    private String origemSelos;
    private boolean exibirDialogSelosPostergado;
    private boolean cargaInicial = false;
    private Boolean editalPesquisa;
    private Boolean postergadoPesquisa;
    private List<ItemLivroProtocolo> encerramentos;
    private ItemLivroProtocolo encerramento;
    private ItemLivroProtocolo encerramentoDelete;
    private Usuario usuarioEncerramento;
    private Usuario usuarioIrregularidade;
    private Usuario usuarioEdital;
    private Usuario usuarioNotificacao;
    private boolean ordemProtesto;
    private List<Map<String, Object>> apresentanteTitulo;
    private List<Map<String, Object>> titulosPorOcorrencia;
    private List<Protesto> titulosParaEdital;
    private List<Protesto> titulosSelecionados;
    private boolean adicionarTitulosMarcados;
    private boolean adicionarTitulosVencidosPendentes;
    private List<AtoGenerico> editaisPublicados;
    private Protocolo processoParticular;
    private List<ControlePagamentoProtesto> controlePagamentosProtesto;
    private ControlePagamentoProtesto controlePagamentoProtesto;
    private UploadedFile arquivo;
    private Anexo anexo;
    private MotivoEditalProtesto motivoEditalProtesto;
    private MotivoSuspensaoProtesto motivoSuspensaoProtesto;
    private String descricaoMotivoSuspensao;
    private TipoSolucaoSuspensaoProtesto tipoSolucaoSuspensaoProtesto;
    private String descricaoSolucaoSuspensao;
    private IrregularidadeProtesto irregularidadeProtesto;
    private TipoDeConsultaDeTitulos tipoDeConsultaDeTitulos;
    private Anexo anexoRemocao;
    private List<Anexo> anexos;
    private boolean marcarEdital;
    private boolean marcarDevolucao;
    private boolean adicionarTaxaBoleto;
    private Usuario usuarioLogado;
    private Usuario usuarioQueAssina;
    private Usuario usuarioQueEscreveu;
    private Protocolo protocoloExibicao;
    private List<Imposto> impostos;
    private BigDecimal valorTaxaBoleto;
    private Ato taxaBoleto;
    private ArrayList<Protesto> boletoProtestoASerCancelado;
    private ArrayList<Protesto> boletosRegerados;
    private Ordenacao ordernacao;
    private Ato atoDespesasConducao;
    private Ato atoDespesasConducaoForaDaCidade;
    private boolean utilizarProcessoApontamento;
    private boolean utilizarProcessoRetirada;
    private boolean utilizarProcessoDevolucao;
    private boolean utilizarProcessoProtesto;
    private boolean utilizarProcessoBaixa;
    private String motivoCancelamentoSelosPostergados;
    private ModeloDocumento modeloDocumentoEdital;
    private StatusProtesto statusBaixaTituloDistribuicao;
    private StreamedContent download;
    private boolean provimentoIntimacaoProv97 = false;
    private Telefone telefoneDevedor;
    private List<StatusProtesto> statusTitulosPedentes;
    private boolean filtrarPorDataDistribuicao;

    @PostConstruct
    public void init() {
        this.selando = false;
        this.cartorio = cartorioServico.obterCartorio();
        instanciar();
        this.configuracao = configuracaoSistemaServico.obterConfiguracao();
        this.usuarioLogado = usuarioServico.getUsuarioLogado();
        this.usuarioAssinatura = usuarioLogado;
        HttpServletRequest uri = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if (uri.getRequestURI().contains("emitirCartas.xhtml")) {
            dataInicio = new Date();
            dataFim = new Date();
            verifyPermission("EMITIR CARTAS");
            Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

            final List<Ato> atosconducao = atoServico.findFilter("protestoDespesasConducao", true);

            Ato atoConducao = !Utils.isEmpty(atosconducao) ? atosconducao.get(0) : null;

            editalValorFixo = true;
            if (atoConducao != null) {
                this.atoDespesasConducao = atoConducao;
                for (AtoCampo atoCampo : atoConducao.getAtoCampos()) {
                    if (atoCampo.getAplicacao().equals(AplicacaoCampo.VALOR)
                            || atoCampo.getAplicacao().equals(AplicacaoCampo.VALOR_CARTORIO)
                            || atoCampo.getAplicacao().equals(AplicacaoCampo.VALOR_SEM_RATEIO)) {
                        editalValorFixo = false;
                    }
                }


                if (configuracao.getValorTaxaEntrega() != null && !editalValorFixo) {
                    this.valorEdital = configuracao.getValorTaxaEntrega();
                } else {
                    List<EmolumentoItem> emolumentosItems = emolumentoItemServico.pesquisarEmolumentos(atoConducao);
                    if (!Utils.isEmpty(emolumentosItems)) {
                        this.valorEdital = emolumentosItems.get(0).getValorTotal();
                    } else {
                        this.valorEdital = BigDecimal.ZERO;
                    }
                }

            }

            List<Ato> atos = atoServico.findFilter("protestoDespesasConducaoForaDaCidade", true);
            if (!Utils.isEmpty(atos)) {
                atoDespesasConducaoForaDaCidade = atos.get(0);
            } else {
                atoDespesasConducaoForaDaCidade = atoDespesasConducao;
            }

            this.tipoEmissaoCartas = parametros.get("emissaoCartaPor");
        } else if (uri.getRequestURI().contains("ordensDeProtesto.xhtml")) {
            dataInicio = new Date();
            dataFim = new Date();
            verifyPermission("EMITIR CARTAS");
            this.ordemProtesto = true;
            Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            this.materializarPor = parametros.get("emissaOrdensPor");
        } else if (uri.getRequestURI().contains("materializarTitulos.xhtml")) {
            dataInicio = new Date();
            dataFim = new Date();
            Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            this.materializarPor = parametros.get("materializarPor");
        } else if (!uri.getRequestURI().contains("indexProtesto.xhtml")) {

            Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            this.protestoid = parametros.get("protestoId");
            this.arquivoId = parametros.get("arquivoId");

            if (!Utils.isEmpty(parametros.get("protocoloId"))) {
                this.protocoloId = Long.parseLong(parametros.get("protocoloId"));
            }

            if (protestoid != null) {

                telefoneDevedor = new Telefone();

                this.exibirDialogSelosPostergado = !Utils.isEmpty(parametros.get("exibirSelos"));
                delegar();
                selosExibicao = new ArrayList<Selo>();
                if (!Utils.isEmpty(parametros.get("exibirSelosDevolucao"))) {
                    origemSelos = "Devolução";
                    this.exibirDialogSelos = true;
                    for (ProtocoloItem protocoloItem : protestoVisualizar.getDevolucao().getProtocoloItems()) {
                        if (protocoloItem.getAtoItem().isPadrao() && protocoloItem.getAtoItem().getTipoSelo() != null) {
                            selosExibicao.addAll(protocoloItem.getSelos());
                        }
                    }
                }

                if (!Utils.isEmpty(parametros.get("exibirSelosPagamento"))) {
                    origemSelos = "Pagamento";
                    this.exibirDialogSelos = true;
                    for (ProtocoloItem protocoloItem : protestoVisualizar.getRetirada().getProtocoloItems()) {
                        if (protocoloItem.getAtoItem().isPadrao() && protocoloItem.getAtoItem().getTipoSelo() != null) {
                            selosExibicao.addAll(protocoloItem.getSelos());
                        }
                    }
                }

                if (!Utils.isEmpty(parametros.get("exibirSelosRetirada"))) {
                    origemSelos = "Devolução";
                    this.exibirDialogSelos = true;
                    for (ProtocoloItem protocoloItem : protestoVisualizar.getRetirada().getProtocoloItems()) {
                        if (protocoloItem.getAtoItem().isPadrao() && protocoloItem.getAtoItem().getTipoSelo() != null) {
                            selosExibicao.addAll(protocoloItem.getSelos());
                        }
                    }
                }
            } else {
                super.instanciar();
            }
        }

        if (uri.getRequestURI().contains("envioSerasa.xhtml") || uri.getRequestURI().contains("gerarArquivoCnp.xhtml")) {
            dataInicio = new Date();
            dataFim = new Date();
            this.protestos = new ArrayList<Protesto>();
            this.titulosPesquisa = null;
            Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            this.tituloSerasa = parametros.get("titulo");
        } else if (uri.getRequestURI().contains("notificacaoIntimacao.xhtml")
                || uri.getRequestURI().contains("emitirEdital.xhtml")
                || uri.getRequestURI().contains("registrarTentativaEntregaProtesto.xhtml")) {
            dataAceite = new Date();
            if (uri.getRequestURI().contains("emitirEdital.xhtml")) {

                this.statusTitulosPedentes = new ArrayList<StatusProtesto>();
                this.statusTitulosPedentes.add(StatusProtesto.APONTADO);
                this.statusTitulosPedentes.add(StatusProtesto.NOTIFICAO_EMITIDA_CARTA);
                this.statusTitulosPedentes.add(StatusProtesto.NOTIFICAO_EMITIDA_EDITAL);

                dataPrazoMin = feriadoServico.proximoDiaUtil(dataAceite, this.configuracao.getPrazoProtestoEdital());
            } else {
                dataPrazoMin = feriadoServico.proximoDiaUtil(dataAceite, this.configuracao.getPrazoProtesto());
            }
            dataPrazo = dataPrazoMin;

            List<Ato> atosEdital = atoServico.findFilter("protestoEdital", true);

            if (!Utils.isEmpty(atosEdital)) {

                Ato atoEdital = atosEdital.get(0);

                editalValorFixo = true;
                if (atoEdital != null && !Utils.isEmpty(atoEdital.getAtoCampos())) {
                    for (AtoCampo atoCampo : atoEdital.getAtoCampos()) {
                        if (atoCampo.getAplicacao().equals(AplicacaoCampo.VALOR)
                                || atoCampo.getAplicacao().equals(AplicacaoCampo.VALOR_CARTORIO)
                                || atoCampo.getAplicacao().equals(AplicacaoCampo.VALOR_SEM_RATEIO)) {
                            editalValorFixo = false;
                        }
                    }
                }

                if (!editalValorFixo && configuracao.getCustaEdital() != null) {
                    valorEdital = configuracao.getCustaEdital();
                } else {
                    List<EmolumentoItem> pesquisarEmolumentos = emolumentoItemServico.pesquisarEmolumentos(atoEdital);
                    if (!Utils.isEmpty(pesquisarEmolumentos)) {

                        EmolumentoItem emolumentoItem = pesquisarEmolumentos.get(0);
                        this.valorEdital = emolumentoItem.getValorTotal();
                    } else {
                        this.valorEdital = BigDecimal.ZERO;
                    }
                }
            }
        } else if (uri.getRequestURI().contains("regerarBoletosProtesto.xhtml")) {
            dataPrazo = feriadoServico.proximoDiaUtil(new Date(), configuracao.getPrazoProtesto());
            List<Ato> obterAtoTaxaBoleto = atoServico.obterAtoTaxaBoleto();
            this.taxaBoleto = null;
            this.valorTaxaBoleto = BigDecimal.ZERO;
            if (!Utils.isEmpty(obterAtoTaxaBoleto)) {
                taxaBoleto = obterAtoTaxaBoleto.get(0);
                ModeloProcesso modelo = modeloProcessoServico.criarModelo(Arrays.asList(taxaBoleto), null, CategoriaProtocolo.REGISTRO, BigDecimal.ZERO);
                this.valorTaxaBoleto = modeloProcessoServico.criarProcesso(modelo).getValorCartorioComDesconto();
            }
            this.protestosEmitirIntimacao = new ArrayList<Protesto>();
            this.boletosRegerados = new ArrayList<Protesto>();
        } else if (uri.getRequestURI().contains("titulosProtestados.xhtml")) {
            this.tipoSituacaoProtestoRelatorio = new ArrayList<TipoSituacaoProtesto>();
            this.tipoSituacaoProtestoRelatorio.add(TipoSituacaoProtesto.PAGO);
            this.tipoSituacaoProtestoRelatorio.add(TipoSituacaoProtesto.PROTESTADO);
            this.tipoSituacaoProtestoRelatorio.add(TipoSituacaoProtesto.DEVOLVIDO_COM_CUSTAS);
            this.tipoSituacaoProtestoRelatorio.add(TipoSituacaoProtesto.DEVOLVIDO_SEM_CUSTAS);
            this.tipoSituacaoProtestoRelatorio.add(TipoSituacaoProtesto.RETIRADO);
            this.dataInicio = new Date();

        } else if (uri.getRequestURI().contains("emitirInstrumentos.xhtml") || uri.getRequestURI().contains("emitirCartas.xhtml")) {
            this.livroProtesto = livroServico.obterLivroProtestoCorrente();
        } else if (uri.getRequestURI().contains("emitirLivroApontamento.xhtml")) {
            this.livroApontamento = livroServico.obterLivroApontamentoCorrente();
        } else if (uri.getRequestURI().contains("relatorioCentraisDeProtesto.xhtml")
                || uri.getRequestURI().contains("titulosProtestados.xhtml")
                || uri.getRequestURI().contains("titulosPagosIndevidamente.xhtml")
                || uri.getRequestURI().contains("relatorioTitulosApontados.xhtml")) {
            dataInicio = new Date();
        } else if (uri.getRequestURI().contains("titulosPendentes.xhtml")) {
            this.protestos = protestoServico.pesquisarTitulosPendentes();
        }
        this.ordernacao = new Ordenacao();
        this.ordernacao.setModo("ASC");
        this.ordernacao.setCampo("codigoApontamento");
        minDate = DateUtils.alterDate(new Date(), -1 * configuracao.getProtestoPrazoMinimoDeMovimentacao());
        devedor = new Pessoa();
        spEditar = new SituacaoProtesto();
    }

    public void removerCliente() {
        this.apresentante = null;
    }

    public void fecharDialogSelos() {
        this.exibirDialogSelos = false;
    }

    public void fecharDialogSelosPostergado() {
        this.exibirDialogSelosPostergado = false;
    }

    public void limparPesquisa() {
        this.protestos = new ArrayList<Protesto>();
        this.situacoesProtesto = new ArrayList<SituacaoProtesto>();
    }

    public void setarApresentante(Pessoa apresentante) {
        this.apresentante = apresentante;
    }

    public void adicionarCustasProtesto() {
        Ato atoDemaisDespesasAvulsa = atoServico.findFilter("protestoDemaisDespesasAvulsa", true).get(0);
        this.custaProtesto.setAto(atoDemaisDespesasAvulsa);
        this.custaProtesto.setDataCadastro(new Date());
        protestoVisualizar.getCustasProtesto().add(custaProtesto);
        protestoServico.update(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Custa adicionada com sucesso!.", "");
    }

    public void adicionarCustasProtesto(Protesto titulo, Ato ato, BigDecimal valor, String descricao) {
        CustaProtesto custaAdicional = new CustaProtesto();
        custaAdicional.setDataCadastro(new Date());
        custaAdicional.setDescricao(descricao);
        custaAdicional.setValor(valor);
        custaAdicional.setUsuario(usuarioServico.getUsuarioLogado());
        custaAdicional.setAto(ato);
        titulo.getCustasProtesto().add(custaAdicional);
    }

    public void adicionarCustasProtestoComAto() {
        this.custaProtesto.setDataCadastro(new Date());
        protestoVisualizar.getCustasProtesto().add(custaProtesto);
        protestoServico.update(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Custa adicionada com sucesso!.", "");
    }

    public String reprAto(Ato ato) {
        if (ato != null && ato.getCodigo() != null) {

            if (cartorio.getEndereco().getCidade().getEstado().getSigla().equals("PE")
                    && ato.getAtoTipo() != null
                    && !Utils.isEmpty(ato.getAtoTipo().getDescricao())
                    && !Utils.isEmpty(ato.getDescricao())) {
                return ato.getAtoTipo().getDescricao().concat("-[").concat(ato.getCodigo().concat("]-").concat(ato.getDescricao()));
            } else {
                return ato.getCodigo().concat(" - ").concat(ato.getDescricao());
            }
        } else {
            return "";
        }
    }

    public List<Ato> autocompletarAtoConducao(String query) {
        Ato atoPesquisar = new Ato();
        atoPesquisar.setCodigo(query);
        atoPesquisar.setSlug(query);
        atoPesquisar.setDescricao(query);
        return atoServico.autocompletarAtoDespesasDeConducao(atoPesquisar);
    }

    public List<Ato> autocompletarAto(String query) {
        Ato atoPesquisar = new Ato();
        atoPesquisar.setCodigo(query);
        atoPesquisar.setSlug(query);
        atoPesquisar.setDescricao(query);


        return atoServico.autocompletarAtoNaAdicaoDeCustas(atoPesquisar);
    }

    public void alterarPrazoProtesto() {
        if (!selando) {
            selando = true;

            SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
            situacaoProtesto.setDataCriacao(new Date());
            situacaoProtesto.setUsuario(usuarioServico.getUserLogged());
            situacaoProtesto.setProtesto(protestoVisualizar);
            situacaoProtesto.setObservacao("Prazo de vencimento recalculado para " + DateUtils.format(protestoVisualizar.getDataPrazoProtesto()));
            situacaoProtesto.setTipoSituacaoProtesto(null);
            situacaoProtestoServico.salvar(situacaoProtesto);

            protestoServico.update(protestoVisualizar);
            if (this.cartorio.isUsaBoleto()) {
                if (this.protestoVisualizar.getDataPrazoEdital() != null) {
                    boletoServico.alterarVencimentoBoleto(protestoVisualizar, this.protestoVisualizar.getDataPrazoEdital());
                } else {

                    boletoServico.alterarVencimentoBoleto(protestoVisualizar, this.protestoVisualizar.getDataPrazoProtesto());
                }
            }

            Mensagem.messagemInfoRedirect("Prazo de vencimento recalculado.", "");
        }
    }

    public List<SelectItem> campos() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("dataCadastro", "Data Apontamento"));
        itens.add(new SelectItem("codigoApontamento", "Apontamento"));
        itens.add(new SelectItem("dataProtesto", "Data Protesto"));
        itens.add(new SelectItem("codigoProtesto", "Cod. Protesto"));
        return itens;
    }

    @Override
    public void instanciar() {
        tipoRelatorio = TipoRelatorioProtesto.TITULOS_PROTESTADOS;
        if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/titulosDistribuidos.xhtml")) {
            tipoRelatorio = TipoRelatorioProtesto.TITULOS_DISTRIBUIDOS;
            tipoDeConsultaDeTitulos = TipoDeConsultaDeTitulos.TODOS;
        }
        protestoPesquisa = new Protesto();
        protestoPesquisa.setDataCadastro(null);

        protestos = new ArrayList<Protesto>();
        apresentante = new Pessoa();
        if (central == 1) {
            numeroRemessa = protestoServico.ultimoNumeroArquivoSerasa() + 1;
            numeroUltimaRemessa = protestoServico.ultimoNumeroArquivoSerasa();
        } else {
            numeroRemessa = protestoServico.ultimoNumeroArquivoBoaVista() + 1;
            numeroUltimaRemessa = protestoServico.ultimoNumeroArquivoBoaVista();
        }
        viaLivro = false;
        this.custaProtesto = new CustaProtesto();
        this.protestosNotificados = new ArrayList<Protesto>();
    }

    @Override
    public void delegar() {
        super.delegar();
        this.protocolos = new ArrayList<Protocolo>();
        protestoVisualizar = protestoServico.pesquisar(Long.parseLong(protestoid));
        controlePagamentoProtesto = new ControlePagamentoProtesto();
        if (protestoVisualizar != null) {

            situacoesProtesto = new ArrayList<SituacaoProtesto>();
            situacoesProtesto = situacaoProtestoServico.situacoesPorProtesto(protestoVisualizar);

            if (protestoVisualizar.getApresentante() != null) {
                apresentantes = new ArrayList();
                apresentante = protestoVisualizar.getApresentante();
                apresentantes.add(apresentante);
            }
            if (protestoVisualizar.getDevedores() != null) {
                devedores = protestoVisualizar.getDevedores();
            }

            if (protestoVisualizar.getSacado() != null) {

                portadores = new ArrayList<Pessoa>();
                portadores.add(protestoVisualizar.getSacado());
            }

            totalProcesso = BigDecimal.ZERO;
            totalCustasProtesto = BigDecimal.ZERO;
            if (!Utils.isEmpty(protestoVisualizar.getCustasProtesto())) {

                for (CustaProtesto custaProtesto : protestoVisualizar.getCustasProtesto()) {
                    totalCustasProtesto = totalCustasProtesto.add(custaProtesto.getValor());
                }
            }


            // CONTROLE DE CUSTAS
            if (this.protestoVisualizar.getTipo().equals(TipoProtesto.PARTICULAR) && this.protestoVisualizar.getProcessoParticular() != null) {

                if (this.protestoVisualizar.getProcessoParticular().getTipoProtocolo().equals(TipoProtocolo.ORCAMENTO)) {
                    this.protestoVisualizar.getProcessoParticular().setDescricao("Orçamento de custas do apontamento");
                } else {
                    this.protestoVisualizar.getProcessoParticular().setDescricao("Antecipação de emolumentos");
                }

                this.protocolos.add(this.protestoVisualizar.getProcessoParticular());
            }

            if (protestoVisualizar.getApontamento() != null) {

                totalProcesso = totalProcesso.add(protestoVisualizar.getApontamento().getValorCartorioComDesconto());
                protestoVisualizar.getApontamento().setDescricao("Apontamento");
                this.protocolos.add(protestoVisualizar.getApontamento());

            } else if (this.protestoVisualizar.getCodigoApontamento() != null && !ProtestoUtils.protestoFinalizado(this.protestoVisualizar)) {

                Protocolo apontamentoTemporario = protestoServico.criarProcessoApontamento72Hrs(protestoVisualizar);
                apontamentoTemporario.setDescricao("Apontamento");
                totalProcesso = totalProcesso.add(apontamentoTemporario.getValorCartorioComDesconto());
                this.protocolos.add(apontamentoTemporario);
            }

            if (this.protestoVisualizar.getProcessoBaixaDistribuicao() != null) {

                this.protestoVisualizar.getProcessoBaixaDistribuicao().setDescricao("Baixa de distribuição");
                totalProcesso = totalProcesso.add(this.protestoVisualizar.getProcessoBaixaDistribuicao().getValorCartorioComDesconto());
                this.protocolos.add(this.protestoVisualizar.getProcessoBaixaDistribuicao());
            }


            if (protestoVisualizar.getRetirada() != null) {

                totalProcesso = totalProcesso.add(protestoVisualizar.getRetirada().getValorCartorioComDesconto());
                protestoVisualizar.getRetirada().setDescricao("Retirada");
                this.protocolos.add(protestoVisualizar.getRetirada());
            }

            if (protestoVisualizar.getProtesto() != null) {

                totalProcesso = totalProcesso.add(protestoVisualizar.getProtesto().getValorCartorioComDesconto());
                protestoVisualizar.getProtesto().setDescricao("Protesto");
                this.protocolos.add(protestoVisualizar.getProtesto());
            } else if ((protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO) || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO_SUSPENSO))
                    && protestoVisualizar.isPostergado() && protestoVisualizar.getBaixa() == null) {

                Protocolo processoBaixaTemporario = protestoServico.criarProcessoProtestoTitulo(protestoVisualizar);

                processoBaixaTemporario.setDescricao("Baixa");
                totalProcesso = totalProcesso.add(processoBaixaTemporario.getValorCartorioComDesconto());
                this.protocolos.add(processoBaixaTemporario);
            } else if (protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO)
                    && !protestoVisualizar.isPostergado()
                    && protestoVisualizar.isApontamentoAutomatico()) {

                Protocolo processoTempProtesto = protestoServico.criarProcessoProtestoTitulo(protestoVisualizar, false);
                processoTempProtesto.setDescricao("Protesto");
                this.protocolos.add(processoTempProtesto);
            }

            if (protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO)
                    && protestoVisualizar.getProtesto() == null && configuracao.isExibirValorProtestoNaBaixa()) {

                Protocolo processoTempProtesto = protestoServico.criarProcessoProtestoTitulo(protestoVisualizar, false);
                processoTempProtesto.setDescricao("Custas Protesto");
                this.protocolos.add(processoTempProtesto);
            }

            if (protestoVisualizar.getStatusProtesto().equals(StatusProtesto.RECEPCIONADO_APENAS)
                    && protestoVisualizar.getTipo().equals(TipoProtesto.DISTRIBUICAO)
                    && protestoVisualizar.getTipo().equals(TipoProtesto.DISTRIBUICAO_PARTICULAR)
                    && protestoVisualizar.getProcessoBaixaDistribuicao() == null) {

                Protocolo processoTempProtesto = protestoServico.criarProcessoDistribuicaoProtesto(protestoVisualizar);
                processoTempProtesto.setDescricao("Custas Distribuição");
                this.protocolos.add(processoTempProtesto);
            }

            if (protestoVisualizar.getBaixa() != null) {
                totalProcesso = totalProcesso.add(protestoVisualizar.getBaixa().getValorCartorioComDesconto());
                protestoVisualizar.getBaixa().setDescricao("Baixa");
                this.protocolos.add(protestoVisualizar.getBaixa());
            } else if (protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO) && (!protestoVisualizar.isPostergado() || protestoVisualizar.getProtesto() != null)) {
                Protocolo baixaTemporaria = protestoServico.criarProcessoBaixa(protestoVisualizar);
                if (baixaTemporaria != null) {
                    baixaTemporaria.setDescricao("Baixa");
                    totalProcesso = totalProcesso.add(baixaTemporaria.getValorCartorioComDesconto());

                    this.protocolos.add(baixaTemporaria);
                } else {
                    Mensagem.messagemWarn("Não foi possível gerar o processo de baixa, contate o administrador.");
                }

            }
            if (protestoVisualizar.getDevolucao() != null) {
                totalProcesso = totalProcesso.add(protestoVisualizar.getDevolucao().getValorCartorioComDesconto());
                protestoVisualizar.getDevolucao().setDescricao("Devolução");
                this.protocolos.add(protestoVisualizar.getDevolucao());
            } else if (protestoVisualizar.getStatusProtesto().equals(StatusProtesto.DEVOLVIDO_COM_CUSTAS)) {
                Protocolo devolucaoTemp = protestoServico.criarProcessoDevolucao(protestoVisualizar);
                if (devolucaoTemp != null) {

                    devolucaoTemp.setDescricao("Devolução");
                    totalProcesso = totalProcesso.add(devolucaoTemp.getValorCartorioComDesconto());

                    this.protocolos.add(devolucaoTemp);
                } else {
                    Mensagem.messagemWarn("Não foi possível gerar o processo de devolução, contate o administrador.");
                }
            }
            this.tipoBaixaProtesto = this.protestoVisualizar.getTipoBaixaProtesto();
            if (this.tipoBaixaProtesto == null) {
                this.tipoBaixaProtesto = TipoBaixaProtesto.CARTA_ANUENCIA;
            }

            this.irregularidades = new ArrayList<IrregularidadeProtesto>();

            if (protestoVisualizar.getCodigoApontamento() != null) {
                editaisPublicados = atoGenericoServico.pesquisarEdital(protestoVisualizar.getCodigoApontamento().intValue(), null, null, null);
            }

            anexos = new ArrayList<Anexo>();
            if (protestoVisualizar.getInstrumentoEletronico() != null) {
                anexos.add(protestoVisualizar.getInstrumentoEletronico());
            }
            if (!Utils.isEmpty(protestoVisualizar.getAnexos())) {
                anexos.addAll(protestoVisualizar.getAnexos());
            }

            if (protestoVisualizar.isPostergado() || configuracao.isSempreDevolverSemCustas()) {
                this.tipoSituacaoProtesto = TipoSituacaoProtesto.DEVOLVIDO_SEM_CUSTAS;
            }
        }
    }

    public void converterEmApontamento() {
        if (!selando) {
            selando = true;
            this.protestoVisualizar.setCodigoAutomatico(true);
            this.protestoVisualizar.setApontamentoAutomatico(true);
            this.protestoVisualizar.setAceite(true);
            this.protestoVisualizar.setDataCadastro(new Date());
            this.protestoVisualizar.setDataAcao(new Date());
            this.protestoVisualizar.setStatusProtesto(StatusProtesto.APONTADO);

            if (configuracao.isSetarPrazoProtestoAoSalvar()) {
                this.protestoVisualizar.setDataPrazoProtesto(feriadoServico.proximoDiaUtil(new Date(), configuracao.getPrazoProtesto()));
            }

            if (protestoVisualizar.getProcessoParticular() != null && protestoVisualizar.getProcessoParticular().getTipoProtocolo().equals(TipoProtocolo.ORCAMENTO)) {
                Protocolo protocoloDP = ProtocoloUtils.converterOrcamento(protestoVisualizar.getProcessoParticular(), protocoloServico);
                protestoVisualizar.setProcessoParticular(protocoloDP);
            }

            protestoServico.update(this.protestoVisualizar);

            SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
            situacaoProtesto.setDataCriacao(new Date());
            Usuario usuarioLogado = usuarioServico.getUsuarioLogado();
            situacaoProtesto.setUsuario(usuarioLogado);
            situacaoProtesto.setObservacao("Título apontado");
            situacaoProtesto.setTipoSituacaoProtesto(TipoSituacaoProtesto.APONTADO);
            situacaoProtesto.setProtesto(protestoVisualizar);
            situacaoProtesto.setActive(true);
            situacaoProtestoServico.salvar(situacaoProtesto);

            Mensagem.messagemInfoRedirect("Título apontado com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
        }
    }

    /**
     * <p> Altera o status e data da ação do titulo, registra o processo de
     * baixa de distribuição, vincula os selos da distribuição no processo
     * criado e gera uma situação protesto da ocorrencia. </p>
     *
     * @author Sávio Araújo
     */
    public void baixarTituloDistribuicao() {
        if (!selando) {
            selando = true;

            ProtestoUtils.baixarTituloDistribuicao(protestoVisualizar, dataAceite, protestoServico, statusBaixaTituloDistribuicao);

            Mensagem.messagemInfoRedirect("Título baixado com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
        }
    }

    public boolean protestoAceite() {
        return protestoVisualizar.getAceite() == null || !protestoVisualizar.getAceite();
    }

    public List<IrregularidadeProtesto> autocompletarIrregularidade(String query) {
        return irregularidadeProtestoServico.autocompletar(query);
    }

    public void registrarIrregularidade() {
        this.protestoVisualizar.setIrregularidadesProtesto(this.irregularidades);
        this.protestoVisualizar.setUsuarioIrregularidades(usuarioIrregularidade);
        this.irregularidade = null;
        this.irregularidades = null;
        devolverTitulo();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("irregularidadesDialog.hide();");
    }

    public void adicionarIrregularidade() {
        if (this.irregularidade != null) {

            if (!this.irregularidades.contains(this.irregularidade)) {
                this.irregularidades.add(irregularidade);
            }
            this.irregularidade = null;
        } else {
            Mensagem.messagemWarn("Selecione a irregularidade");
        }
    }

    public void removerIrregularidade(IrregularidadeProtesto irregularidade) {
        this.irregularidades.remove(irregularidade);
    }

    public boolean emitirIntimacao() {
        return protestoVisualizar.getStatusProtesto() != null && (protestoVisualizar.getStatusProtesto() == StatusProtesto.APONTADO
                || protestoVisualizar.getStatusProtesto() == StatusProtesto.CADASTRADO
                || protestoVisualizar.getStatusProtesto() == StatusProtesto.NOTIFICAO_EMITIDA_EDITAL
                || protestoVisualizar.getStatusProtesto() == StatusProtesto.NOTIFICAO_EMITIDA_CARTA
                || protestoVisualizar.getStatusProtesto() == StatusProtesto.CADASTRADO_POR_IMPORTACAO)
                && protestoVisualizar.getAceite() != null && protestoVisualizar.getAceite();
    }

    public boolean protestoNaoEncerrado() {
        return !(protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO)
                || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO_SUSPENSO)
                || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.CANCELAMENTO)
                || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.BAIXADO)
                || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO_PAGO)
                || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.DEVOLVIDO)
                || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.DEVOLVIDO_COM_CUSTAS)
                || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.DEVOLVIDO_SEM_CUSTAS)
                || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.RETIRADO));
    }

    public boolean protestoNaoPago() {

        if (protestoVisualizar.getApontamento() != null && !protestoVisualizar.getApontamento().isQuitado()) {
            return true;
        }

        if (protestoVisualizar.getProtesto() != null && !protestoVisualizar.getProtesto().isQuitado()) {
            return true;
        }

        if (protestoVisualizar.getBaixa() != null && !protestoVisualizar.getBaixa().isQuitado()) {
            return true;
        }

        if (protestoVisualizar.getRetirada() != null && !protestoVisualizar.getRetirada().isQuitado()) {
            return true;
        }
        return false;
    }

    public void alterarVencimento() {
        dataPrazoMin = feriadoServico.proximoDiaUtil(dataAceite, this.configuracao.getPrazoProtesto());
        dataPrazo = dataPrazoMin;
    }

    public void alterarVencimentoEdital() {
        dataPrazoMin = feriadoServico.proximoDiaUtil(dataAceite, this.configuracao.getPrazoProtestoEdital());
        dataPrazo = dataPrazoMin;
    }

    public boolean protestoIntimacaoEdital() {
        return protestoNaoEncerrado() && !protestoVisualizar.isIntimacaoPorEdital() && protestoVisualizar.getAceite() != null && protestoVisualizar.getAceite();
    }

    public boolean protestoNotificado() {
        return protestoVisualizar.getStatusProtesto().equals(StatusProtesto.NOTIFICAO_EMITIDA_CARTA) || protestoVisualizar.getStatusProtesto().equals(StatusProtesto.NOTIFICAO_EMITIDA_EDITAL);
    }

    public void alterarStatus() {
        protestoServico.update(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Status Alterado com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void aceitarProtesto() {
        protestoVisualizar.setAceite(true);
        protestoServico.update(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Protesto aceito.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void notificacaoAceita() {

        protestoServico.notificarProtesto(protestoVisualizar, null, null, usuarioNotificacao);
        Mensagem.messagemInfoRedirect("Devedor notificado, o protesto vence em " + DateUtils.format(protestoVisualizar.getDataPrazoProtesto()), "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void protestoBaixado() {
        this.protestoVisualizar.setTipoBaixaProtesto(tipoBaixaProtesto);
        this.protestoServico.update(this.protestoVisualizar);
        if (this.configuracao.isSelagemManual()) {
            Mensagem.messagemInfoRedirect("Retirado com sucesso", "selarProtocoloManual.xhtml?url=visualizarProtesto&parametro=protestoId&objetoId=" + protestoVisualizar.getId());
        } else {
            Mensagem.messagemInfoRedirect("Retirado com sucesso", "selarProtocolo.xhtml?url=visualizarProtesto&parametro=protestoId&objetoId=" + protestoVisualizar.getId() + "&protocoloId=" + protestoVisualizar.getBaixa().getId());
        }
    }

    public void cancelarIrregularidade() {
        this.irregularidade = null;
        this.irregularidades = null;
    }

    public void devolverTitulo() {
        ProtestoUtils.devolverTitulo(protestoVisualizar, tipoSituacaoProtesto, protocoloItemServico, atoGenericoServico, protestoServico, protocoloServico, situacaoProtestoServico, usuarioServico.getUsuarioLogado(), this.configuracao);
        if (protestoVisualizar.getDevolucao() == null) {
            Mensagem.messagemInfoRedirect("Titulo devolvido com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
        } else if (configuracao.isSelarDevolucaoAutomatica()) {
            String tipoSeloErro = SelagemHelper.selar(selarServico, seloServico, protestoVisualizar.getDevolucao(), protestoVisualizar.getLivroApontamento(), protestoVisualizar.getFolhaApontamento(), null);
            if (!Utils.isEmpty(tipoSeloErro)) {
                tipoSeloErro = tipoSeloErro.replaceFirst(",", "");
                Mensagem.messagemWarnRedirect("Estoque de selo insuficiente para os tipos : " + tipoSeloErro, "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId() + "&exibirSelosDevolucao=true");
            } else if (protestoVisualizar.getDevolucao() != null) {
                Mensagem.messagemInfoRedirect("Titulo devolvido com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId() + "&exibirSelosDevolucao=true");
            }
        } else if (this.configuracao.isSelagemManual()) {
            Mensagem.messagemInfoRedirect("Devolvido com sucesso", "selarProtocoloManual.xhtml?url=visualizarProtesto&parametro=protestoId&objetoId=" + protestoVisualizar.getId() + "&classe=PROTESTO");
        } else {
            Mensagem.messagemInfoRedirect("Devolvido com sucesso", "selarProtocolo.xhtml?url=visualizarProtesto&parametro=protestoId&objetoId=" + protestoVisualizar.getId() + "&classe=PROTESTO");
        }
    }

    public void protestoRetirado() {
        ProtestoUtils.retirarTitulo(protestoVisualizar, protocoloItemServico, atoGenericoServico, protestoServico, protocoloServico, situacaoProtestoServico, usuarioServico.getUsuarioLogado(), this.configuracao);
        protestoVisualizar = protestoServico.load(protestoVisualizar.getId());

        if (protestoVisualizar.getRetirada() == null) {
            Mensagem.messagemInfoRedirect("Titulo retirado com sucesso ", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
        } else if (configuracao.isSelarRetiradaAutomatica() && this.protestoVisualizar.getRetirada() != null) {
            String tipoSeloErro = SelagemHelper.selar(selarServico, seloServico, protestoVisualizar.getRetirada(), protestoVisualizar.getLivroApontamento(), protestoVisualizar.getFolhaApontamento(), null);
            if (!Utils.isEmpty(tipoSeloErro)) {
                tipoSeloErro = tipoSeloErro.replaceFirst(",", "");
                Mensagem.messagemWarnRedirect("Estoque de selo insuficiente para os tipos : " + tipoSeloErro, "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId() + "&exibirSelosRetirada=true");
            } else {
                Mensagem.messagemInfoRedirect("Titulo retirado com sucesso ", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId() + "&exibirSelosRetirada=true");
            }
        } else if (this.configuracao.isSelagemManual()) {
            Mensagem.messagemInfoRedirect("Retirado com sucesso", "selarProtocoloManual.xhtml?url=visualizarProtesto&parametro=protestoId&objetoId=" + protestoVisualizar.getId() + "&classe=PROTESTO");
        } else {
            Mensagem.messagemInfoRedirect("Retirado com sucesso", "selarProtocolo.xhtml?url=visualizarProtesto&parametro=protestoId&objetoId=" + protestoVisualizar.getId() + "&classe=PROTESTO");
        }
    }

    public void notificarPorEdital() {
        protestoVisualizar.setIntimacaoPorEdital(true);
        protestoServico.update(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Protesto será incluso no edital.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void selarApontamentoPostergado() {
        if (!selando) {
            selando = true;
            ProtestoUtils.selarTituloPostergadoAtosApontamento(protestoVisualizar, configuracao, protestoServico, seloServico);
            Mensagem.messagemInfoRedirect("Selado com sucesso!", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId() + "&exibirSelos=true");
            selando = false;
        }
    }

    public void selarPostergado() {
        if (!selando) {
            selando = true;
            ProtestoUtils.selarTituloPostergado(protestoVisualizar, configuracao, protestoServico, seloServico);
            Mensagem.messagemInfoRedirect("Selado com sucesso!", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId() + "&exibirSelos=true");
            selando = false;
        }
    }

    public void selarDistribuicao() {
        if (!selando) {
            try {
                selando = true;
                ProtestoUtils.selarDistribuicaoProtesto(protestoVisualizar, configuracao, protestoServico, seloServico);
                Mensagem.messagemInfoRedirect("Selado com sucesso!", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId() + "&exibirSelos=true");
                selando = false;
            } catch (ProtestoException ex) {
                selando = false;
                Mensagem.messagemError(ex.getMessage());
                Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void pagamentoProcesso(Protocolo processo) {

//        if (!Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Baixa") && protestoVisualizar.isPostergado() && !protestoVisualizar.isAutorizacaoCancelamentoProtesto()) {
//            Mensagem.messagemWarn("Não foi registrado a autorização de baixa deste título, por favor verifique se há arquivos de aceites pendentes de importação no notário.");
//            return;
//        }

        // Criar o processo se for necessário
        if (processo.getId() == null) {

            protocoloServico.salvar(processo);
            processo = protocoloServico.pesquisar(processo.getId());
            if (protestoVisualizar.getApontamento() == null && !Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Apontamento")) {
                protestoVisualizar.setApontamento(processo);
            } else if (protestoVisualizar.getBaixa() == null && !Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Baixa")) {
                protestoVisualizar.setBaixa(processo);
            } else if (protestoVisualizar.getDevolucao() == null && !Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Devolução")) {
                protestoVisualizar.setDevolucao(processo);
            } else if (protestoVisualizar.getProtesto() == null && !Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Protesto")) {
                protestoVisualizar.setProtesto(processo);
            }
            protestoServico.update(protestoVisualizar);
        }
        if (UserUtils.hasAccess(usuarioLogado, "CAIXA", "PAGAMENTO DE PROTOCOLO")) {
            Mensagem.redirect(obterURLPagamento(processo));
        } else {
            Mensagem.redirect("visualizarProtocolo.xhtml?protocoloId=" + processo.getId());
        }
    }

    public void gerarProcesso(Protocolo processo) {

//        if (!Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Baixa") && protestoVisualizar.isPostergado() && !protestoVisualizar.isAutorizacaoCancelamentoProtesto()) {
//            Mensagem.messagemWarn("Não foi registrado a autorização de baixa deste título, por favor verifique se há arquivos de aceites pendentes de importação no notário.");
//            return;
//        }

        // Criar o processo se for necessário
        if (processo.getId() == null) {

            protocoloServico.salvar(processo);
            processo = protocoloServico.pesquisar(processo.getId());
            if (protestoVisualizar.getApontamento() == null && !Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Apontamento")) {
                protestoVisualizar.setApontamento(processo);
            } else if (protestoVisualizar.getBaixa() == null && !Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Baixa")) {
                protestoVisualizar.setBaixa(processo);
            } else if (protestoVisualizar.getDevolucao() == null && !Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Devolução")) {
                protestoVisualizar.setDevolucao(processo);
            } else if (protestoVisualizar.getProtesto() == null && !Utils.isEmpty(processo.getDescricao()) && processo.getDescricao().equals("Protesto")) {
                protestoVisualizar.setProtesto(processo);
            }
            protestoServico.update(protestoVisualizar);
        }

        Mensagem.redirect("visualizarProtocolo.xhtml?protocoloId=" + processo.getId());

    }

    public void pagarProtesto() throws ParseException {

        Date dataAtual = new Date();
        dataAtual = DateUtils.alterTime(DateUtils.toDate(DateUtils.format(dataAtual), "dd/MM/yyyy"), 0, 0, 0);
        Date dataVencimento = protestoVisualizar.getDataPrazoProtesto() != null ? DateUtils.alterTime(protestoVisualizar.getDataPrazoProtesto(), 0, 0, 0) : null;
        boolean equals = dataAtual.equals(dataVencimento);
        // Pagar somente o apontamento, quando ainda não estiver vencido
        if (dataVencimento == null || dataAtual.before(dataVencimento) || equals) {
            // para caso não exista o processo de apontamento
            if (protestoVisualizar.getApontamento() == null) {
                Protocolo protocoloApontamento = protestoServico.criarProcessoApontamento72Hrs(protestoVisualizar);
                protocoloServico.salvar(protocoloApontamento);
                protestoVisualizar.setApontamento(protocoloApontamento);
                protestoServico.atualizar(protestoVisualizar);
            }
            Mensagem.redirect("visualizarProtocolo.xhtml?protocoloId=" + protestoVisualizar.getApontamento().getId());
            // Pagar somente o apontamento, quando ainda não estiver vencido    
        } else if (protestoNaoEncerrado()) {
            if (protestoVisualizar.getProtesto() == null) {
                Protocolo protocoloProtesto = protestoServico.criarProcessoProtestoTitulo(protestoVisualizar);
                protocoloServico.salvar(protocoloProtesto);
                protestoVisualizar.setProtesto(protocoloProtesto);
                protestoServico.atualizar(protestoVisualizar);
            }
            Mensagem.redirect("visualizarProtocolo.xhtml?protocoloId=" + protestoVisualizar.getProtesto().getId());
        }
    }

    public void salvarDevedorCorrecao() {

        if (telefoneDevedor != null && !Utils.isEmpty(telefoneDevedor.getNumero())) {

            telefoneDevedor.setNumero(Caracteres.removecaracter(telefoneDevedor.getNumero()));

            telefoneDevedor.setTipoTelefone(TipoTelefone.CELULAR);

            devedorCorrecao.setTelefones(Arrays.asList(telefoneDevedor));

        }

        pessoaServico.update(devedorCorrecao);
        Mensagem.messagemInfoRedirect("Devedor alterado com sucesso!", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void pesquisar() {

        devedor.setCnpj(null);
        if (Caracteres.removecaracter(devedor.getCpf()).length() != 11) {
            devedor.setCnpj(devedor.getCpf());
        }
        protestos = protestoServico.pesquisar(protestoPesquisa, devedor, processo, editalPesquisa, postergadoPesquisa, this.ordernacao);
    }

    public void deletar() {
        // super.delete();
        protestoServico.deletar(protestoVisualizar, situacoesProtesto);
        Mensagem.messagemInfoRedirect("Protesto excluído com sucesso.", "pesquisarProtesto.xhtml");
    }

    public void exibirCustasProtesto(Protocolo protocolo) {
        this.protocoloExibicao = protocolo;
        this.impostos = impostoServico.pesquisarImpostosVigente();
        RequestContext.getCurrentInstance().execute("detalhesProtocoloWidget.show();");
    }

    public void executarAcao(String acao) {

        if (acao.equals("PROTESTAR")) {
            this.protestoVisualizar.setStatusProtesto(StatusProtesto.PROTESTO);
        } else if (acao.equals("CANCELAR_PROTESTO")) {
            this.protestoVisualizar.setStatusProtesto(StatusProtesto.CANCELAMENTO);
        } else if (acao.equals("SUSPENDER_PROTESTO")) {
//            this.protestoVisualizar.setStatusProtesto(StatusProtesto.SUSPENSAO);
        } else if (acao.equals("REVOGAR_CANCELAMENTO")) {
//            this.protestoVisualizar.setStatusProtesto(StatusProtesto.REVOGACAO_CANCELAMENTO);
        } else if (acao.equals("REVOGAR_SUSPENSAO")) {
//            this.protestoVisualizar.setStatusProtesto(StatusProtesto.REVOGACAO_SUSPENSAO);
        }

        protestoServico.atualizar(this.protestoVisualizar);
        salvarSituacaoProtesto(this.protestoVisualizar);
        if (acao.equals("REVOGAR_CANCELAMENTO") || acao.equals("REVOGAR_SUSPENSAO")) {
            this.protestoVisualizar.setStatusProtesto(StatusProtesto.PROTESTO);
            protestoServico.atualizar(this.protestoVisualizar);
        }
    }

    public void salvarSituacaoProtesto(Protesto protesto) {
        SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
        situacaoProtesto.setDataCriacao(new Date());
        situacaoProtesto.setUsuario(usuarioServico.getUsuarioLogado());
        situacaoProtesto.setProtesto(protesto);
        situacaoProtestoServico.salvar(situacaoProtesto);

        String url = "visualizarProtesto.xhtml?protestoId=" + protesto.getId();
        Mensagem.messagemInfoRedirect("Ação executada com sucesso.", url);
    }

    public void uploadInstrumento() {
        try {
            if (this.anexoRemocao != null) {
                try {
                    anexoServico.excluirArquivo(new File(anexoRemocao.getUrl() + anexoRemocao.getNome()));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (protestoVisualizar.getInstrumentoEletronico() != null) {
                Anexo instrumentoEletronico = protestoVisualizar.getInstrumentoEletronico();
                anexoServico.gravarAnexo(instrumentoEletronico);

                protestoVisualizar = protestoServico.pesquisar(protestoVisualizar.getId());
                protestoVisualizar.setInstrumentoEletronico(instrumentoEletronico);
            } else {
                protestoVisualizar.setInstrumentoEletronico(null);
            }

            protestoServico.update(protestoVisualizar);
            Mensagem.messagemInfoRedirect("Instrumento anexado com sucesso!", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
        } catch (IOException ex) {
            Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancelarUploadInstrumento() {
        this.anexoRemocao = null;
    }

    public List<Integer> formatarCodigosProtesto(String codigosProtestos) {

        if (Utils.isEmpty(codigosProtestos)) {
            return new ArrayList<Integer>();
        }

        String[] codigos = codigosProtestos.trim().split(";");
        String[] codigo;
        List<Integer> codigosProtesto = new ArrayList<Integer>();

        if (codigos.length == 1 && !codigos[0].contains(",") && !codigos[0].contains("-")
                && codigos[0] != null && !codigos[0].isEmpty()) {
            codigosProtesto.add(Integer.parseInt(codigos[0]));
            return codigosProtesto;
        }

        for (String sequencia : codigos) {

            if (sequencia.trim().contains(",")) {

                codigo = sequencia.trim().split(",");

                for (String string : codigo) {
                    codigosProtesto.add(Integer.parseInt(string));
                }


            } else if (sequencia.trim().contains("-")) {
                codigo = sequencia.trim().split("-");

                if (Integer.parseInt(codigo[0]) > Integer.parseInt(codigo[1])) {
                    for (int i = Integer.parseInt(codigo[1]); i <= Integer.parseInt(codigo[0]); i++) {
                        codigosProtesto.add(new Integer(i));
                    }
                } else {
                    for (int i = Integer.parseInt(codigo[0]); i <= Integer.parseInt(codigo[1]); i++) {
                        codigosProtesto.add(new Integer(i));
                    }
                }
            } else if (sequencia != null && !sequencia.isEmpty()) {
                codigosProtesto.add(new Integer(sequencia));
            }
        }

        return codigosProtesto;
    }

    public void preEmitirApontamentos() {
        RequestContext.getCurrentInstance().execute("printButton.jq.click();");
    }

    public void emitirTitulosPesquisa() {

        Map<String, Object> parametros = RelatorioUtil.relatorioCabecalho(cartorioServico.obterCartorio());
        Locale local = new Locale("pt", "BR");
        DateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy ' às ' HH:mm", local);
        parametros.put("data", dateFormat.format(new Date()) + " por " + usuarioLogado.getNome());
        parametros.put("tituloRelatorio", "RELATÓRIO DE TÍTULOS");

        String titulo = "";

        if (protestoPesquisa.getStatusProtesto() != null) {
            titulo += ", Status " + protestoPesquisa.getStatusProtesto().getNome();
        }

        if (protestoPesquisa.getDataProtesto() != null) {
            titulo += ", Data Protesto " + DateUtils.format(protestoPesquisa.getDataProtesto());
        }

        if (protestoPesquisa.getDataCadastro() != null) {
            titulo += ", Data Apontamento " + DateUtils.format(protestoPesquisa.getDataCadastro());
        }

        if (protestoPesquisa.getEspecieTitulo() != null) {
            titulo += ", Espécie do Titulo " + protestoPesquisa.getEspecieTitulo().getSigla() + " - " + protestoPesquisa.getEspecieTitulo().getDescricao();
        }

        if (!Utils.isEmpty(devedor)) {
            titulo += ", Devedor " + (!Utils.isEmpty(devedor.getNome()) ? devedor.getNome() : "") + (!Utils.isEmpty(devedor.getCpf()) ? "CPF " + devedor.getCpf() : "");
        }

        titulo = titulo.replaceFirst(", ", "");
        parametros.put("texto", "Filtros: " + titulo);

        RelatorioServico.gerarRelatorio(parametros, protestos, Relatorio.PROTESTO_RELACAO_SIMPLES);
    }

    public void emitirTitulosApontados() {
        Map<String, Object> parametros = RelatorioUtil.relatorioCabecalho(cartorioServico.obterCartorio());
        Locale local = new Locale("pt", "BR");
        DateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy ' às ' HH:mm", local);
        parametros.put("data", dateFormat.format(new Date()) + " por " + usuarioLogado.getNome());
        parametros.put("tituloRelatorio", "RELATÓRIO DE TÍTULOS APONTADOS");
        String titulo = "Data " + DateUtils.format(dataInicio);

        if (dataFim != null) {
            titulo += " à " + DateUtils.format(dataFim);
        }
        parametros.put("texto", titulo);

        RelatorioServico.gerarRelatorio(parametros, protestos, Relatorio.PROTESTO_RELACAO_SIMPLES);
    }

    public void emitirTitulosPendentes() {
        Map<String, Object> parametros = RelatorioUtil.relatorioCabecalho(cartorioServico.obterCartorio());
        Locale local = new Locale("pt", "BR");
        DateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy ' às ' HH:mm", local);
        parametros.put("data", dateFormat.format(new Date()) + " por " + usuarioLogado.getNome());
        parametros.put("tituloRelatorio", "RELATÓRIO DE TÍTULOS PENDENTES");
        parametros.put("texto", "Títulos Pendentes");

        RelatorioServico.gerarRelatorio(parametros, protestos, Relatorio.PROTESTO_TITULOS_PENDENTES);
    }

    public void emitirApontamentos() {
        protestoServico.emitirLivroApontamentoContinuo(livroApontamento, dataInicio, dataFim);
    }

    public void emitirDistribuicao() {
        protestoServico.emitirLivroDistribuicaoContinuo(livroApontamento, dataInicio, dataFim);
    }

    public void emitirInstrumentos() throws FileNotFoundException, IOException, DocumentException {
        List<Protesto> protestos = protestoServico.emitirLivroPorPeriodo(livroProtesto, dataInicio, dataFim);
        protestoServico.emitirInstrumentos(protestos, viaLivro);
    }

    public void preProcessamentoOrdensProtesto() {
        this.protestos = null;
        if (!Utils.isEmpty(this.codigosApontamentos) || dataInicio != null) {
            if (!Utils.isEmpty(this.materializarPor)) {
                if (materializarPor.equals("CODIGOS")) {
                    protestos = protestoServico.obterProtestosOrdemsParaEmissao(formatarCodigosProtesto(this.codigosApontamentos), null, null, null);
                } else {
                    protestos = protestoServico.obterProtestosOrdemsParaEmissao(null, dataInicio, dataFim, protestoPesquisa.getStatusProtesto());
                }
            } else {
                protestos = protestoServico.obterProtestosOrdemsParaEmissao(null, dataInicio, dataFim, protestoPesquisa.getStatusProtesto());
            }
        }

        if (!Utils.isEmpty(this.protestos)) {
            this.codigosApontamentos = "";
            RequestContext.getCurrentInstance().execute("materializarTitulosBtn.jq.click();");

        } else {
            if (materializarPor.equals("CODIGOS")) {
                Mensagem.messagemWarn("Títulos não encontrados");
            } else {
                if (protestoPesquisa.getStatusProtesto() == null) {
                    Mensagem.messagemWarn("Não foram encontrados alterações de status no periodo informado.");
                } else {
                    Mensagem.messagemWarn("Não foram encontrados alterações de status para " + protestoPesquisa.getStatusProtesto().getNome() + " no periodo informado.");
                }
            }
        }
    }

    public void preProcessamentoMaterializar() {
        this.protestos = null;
        if (!Utils.isEmpty(this.codigosApontamentos) || dataInicio != null) {
            if (!Utils.isEmpty(this.materializarPor)) {
                if (materializarPor.equals("CODIGOS")) {
                    protestos = protestoServico.obterProtestosMaterializar(formatarCodigosProtesto(this.codigosApontamentos));
                } else if (materializarPor.equals("DATA")) {
                    protestos = protestoServico.obterProtestosMaterializar(dataInicio, dataFim);
                } else {
                    protestos = protestoServico.obterProtestosMaterializar(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
                }
            } else {
                protestos = protestoServico.obterProtestosMaterializar(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
            }
        }

        if (!Utils.isEmpty(this.protestos)) {
            this.codigosApontamentos = "";
            RequestContext.getCurrentInstance().execute("materializarTitulosBtn.jq.click();");

        } else {
            Mensagem.messagemWarn("Não foi possivel emitir os títulos informados, verifique se os títulos estão APONTADOS ou NOTIFICADOS POR CARTA/EDITAL.");
        }

    }

    public void materializarTitulos() throws IOException, DocumentException {
        List<FileInputStream> titulosMaterializados = new ArrayList<FileInputStream>();

        if (ordemProtesto) {
            for (Protesto protesto : protestos) {
                File tituloMaterializado = protestoServico.ordemDeProtesto(protesto, true);
                titulosMaterializados.add(new FileInputStream(tituloMaterializado));

            }
        } else {
            for (Protesto protesto : protestos) {
                File tituloMaterializado = protestoServico.materializarTitulo(protesto, true);
                titulosMaterializados.add(new FileInputStream(tituloMaterializado));
            }
        }
        RelatorioServico.gerarRelatorioUnico(titulosMaterializados);
    }

    public void preProcessamentoCartas() {
        preProcessamentoCartas(true);
    }

    public void preProcessamentoCartas(boolean imprimir) {
        this.protestos = null;
        boolean existemTitulosComCustasHoje = false;
        if (!Utils.isEmpty(this.codigosApontamentos) || dataInicio != null) {
            if (!Utils.isEmpty(this.tipoEmissaoCartas)) {
                if (tipoEmissaoCartas.equals("CODIGOS")) {
                    protestos = protestoServico.obterProtestosIntimacaoCarta(formatarCodigosProtesto(this.codigosApontamentos));
                    existemTitulosComCustasHoje = protestoServico.existemTitulosJaComcustasDoDia(formatarCodigosProtesto(this.codigosApontamentos));
                } else if (tipoEmissaoCartas.equals("DATA")) {

                    if (!filtrarPorDataDistribuicao) {
                        protestos = protestoServico.obterProtestosIntimacaoCarta(dataInicio, dataFim);
                    } else {
                        protestos = protestoServico.obterProtestosIntimacaoCartaPorDataDistribuicao(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
                    }

                    existemTitulosComCustasHoje = protestoServico.existemTitulosJaComcustasDoDia(dataInicio, dataFim);
                } else {
                    protestos = protestoServico.obterProtestosIntimacaoCarta(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
                    existemTitulosComCustasHoje = protestoServico.existemTitulosJaComcustasDoDia(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
                }
            } else {

                if (!filtrarPorDataDistribuicao) {
                    protestos = protestoServico.obterProtestosIntimacaoCarta(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
                    existemTitulosComCustasHoje = protestoServico.existemTitulosJaComcustasDoDia(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
                } else {
                    protestos = protestoServico.obterProtestosIntimacaoCartaPorDataDistribuicao(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
                    existemTitulosComCustasHoje = protestoServico.existemTitulosJaComcustasDoDiaPorDataDistribuicao(formatarCodigosProtesto(this.codigosApontamentos), dataInicio, dataFim);
                }
            }
        }

        if (!Utils.isEmpty(this.protestos)) {
            this.codigosApontamentos = "";
            if (imprimir) {
                if (existemTitulosComCustasHoje) {
                    RequestContext.getCurrentInstance().execute("confirmarCustasDialog.show();");
                } else {
                    RequestContext.getCurrentInstance().execute("emitirCartasBtn.jq.click();");
                }
            }
        } else {
            Mensagem.messagemWarn("Não foi possivel emitir os títulos informados, verifique se os títulos estão APONTADOS ou NOTIFICADOS POR CARTA/EDITAL.");
        }

    }

    public void emitirOrdemDeDevolucao() {
        protestoServico.emitirOrdemEstornoProtesto(protestoVisualizar);
    }

    public void emitirOrdemPagamentoParticular() {

        List<ImpostoVO> impostosList = new ArrayList<ImpostoVO>();
        List<Imposto> impostos = impostoServico.pesquisarImpostosVigente();
        processoParticular = protestoVisualizar.getProcessoParticular();
        // Para calcular os impostos na impressão é necessário passar esse VO
        for (ImpostoProtocolo impostoProtocolo : processoParticular.getImpostos()) {
            impostosList.add(new ImpostoVO(impostoProtocolo.getImposto(), impostoProtocolo.getValorTotalComDesconto()));
        }
        protocoloServico.gerarComprovante(processoParticular, processoParticular.getValorCartorioComDesconto(), processoParticular.getProtocoloItems(), impostosList, false, false, false);
    }

    public void emitirBoletosRegerados() throws IOException, DocumentException {
        Usuario usuarioLogado = usuarioServico.getUsuarioLogado();
        List<FileInputStream> cartas = new ArrayList<FileInputStream>();

        Date dataAtual = new Date();
        for (Protesto protesto : protestosEmitirIntimacao) {
            try {
                if (!ProtestoUtils.protestoFinalizado(protesto)
                        && !protesto.getStatusProtesto().equals(StatusProtesto.PROTESTO_NOTIFICADO_POR_CARTA)
                        && !protesto.getStatusProtesto().equals(StatusProtesto.PROTESTO_NOTIFICADO_POR_EDITAL)) {

                    protesto.setStatusProtesto(StatusProtesto.NOTIFICAO_EMITIDA_CARTA);
                    protesto.setDataAcao(dataAtual);
                    protestoServico.atualizar(protesto);
                }

                Boleto boleto = boletoServico.obterBoleto(protesto, configuracao, usuarioLogado);

                SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
                situacaoProtesto.setDataCriacao(dataAtual);
                situacaoProtesto.setUsuario(usuarioServico.getUserLogged());
                situacaoProtesto.setProtesto(protesto);
                situacaoProtesto.setTipoSituacaoProtesto(TipoSituacaoProtesto.EMITIR_CARTA);
                situacaoProtestoServico.salvar(situacaoProtesto);

                if (boleto != null) {
                    boleto = boletoServico.pesquisar(boleto.getId());
                    File file = File.createTempFile("carta-temp-protesto", ".pdf");
                    FileUtils.writeByteArrayToFile(file, boletoServico.gerarBoletoProtestoByte(boleto));

                    cartas.add(new FileInputStream(file));
                }
            } catch (BoletoException ex) {
                Mensagem.messagemError(ex.getMessage());
            }
        }

        RelatorioServico.gerarRelatorioUnico(cartas);
    }

    public void emitirCartas() throws IOException, DocumentException {
        this.emitirCartas(true);
    }

    public void emitirCartas(boolean adicionarCustasNovamente) throws IOException, DocumentException {
        usuarioLogado = usuarioServico.getUsuarioLogado();
        if (cartorio.isUsaBoleto()) {
            List<FileInputStream> cartas = new ArrayList<FileInputStream>();

            Date dataAtual = new Date();
            List<Protesto> listSemDuplicidade = new ArrayList<Protesto>();
            for (Protesto protesto : protestos) {
                // contorna a duplicidade dos titulos
                if (!listSemDuplicidade.contains(protesto)) {
                    listSemDuplicidade.add(protesto);
                } else {
                    continue;
                }

                if (protesto.getDataPrazoProtesto() == null) {
                    protesto.setDataPrazoProtesto(feriadoServico.proximoDiaUtil(new Date(), configuracao.getPrazoProtesto()));
                }

                try {
                    Boleto boleto;
                    if (!ProtestoUtils.protestoFinalizado(protesto)
                            && !protesto.getStatusProtesto().equals(StatusProtesto.PROTESTO_NOTIFICADO_POR_CARTA)
                            && !protesto.getStatusProtesto().equals(StatusProtesto.PROTESTO_NOTIFICADO_POR_EDITAL)) {

                        if (adicionarCustas && adicionarCustasNovamente) {

                            if (!editalValorFixo) {
                                Ato atoConducao = atoDespesasConducao;
                                if (atoConducao == null) {
                                    atoConducao = atoServico.findFilter("protestoDespesasConducao", true).get(0);
                                }

                                CustaProtesto custa = ProtestoUtils.adicionarCusta(protesto, Arrays.asList(atoConducao), valorEdital, usuarioEdital, modeloProcessoServico, new Date());
                                if (custa != null) {
                                    custa.setValor(valorEdital);
                                }

                            } else {
                                if (!protesto.isForaDaCidade()) {
                                    protestoServico.adicionarCustasProtesto(dataAtual, atoDespesasConducao, usuarioLogado, protesto);
                                } else {
                                    protestoServico.adicionarCustasProtesto(dataAtual, atoDespesasConducaoForaDaCidade, usuarioLogado, protesto);
                                }
                            }
                        }

                        protesto.setProvimentoIntimacaoProv97(provimentoIntimacaoProv97);

                        boleto = boletoServico.obterBoleto(protesto, configuracao, usuarioLogado);
                        protesto.setStatusProtesto(StatusProtesto.NOTIFICAO_EMITIDA_CARTA);
                        protesto.setDataAcao(dataAtual);
                        protestoServico.atualizar(protesto);
                        protesto = protestoServico.pesquisar(protesto.getId());

                    } else {
                        protesto.setProvimentoIntimacaoProv97(provimentoIntimacaoProv97);
                        boleto = boletoServico.obterBoleto(protesto, configuracao, usuarioLogado);
                    }

                    SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
                    situacaoProtesto.setProvimentoIntimacaoProv97(provimentoIntimacaoProv97);
                    situacaoProtesto.setDataCriacao(dataAtual);
                    situacaoProtesto.setUsuario(usuarioServico.getUserLogged());
                    situacaoProtesto.setProtesto(protesto);
                    situacaoProtesto.setTipoSituacaoProtesto(TipoSituacaoProtesto.EMITIR_CARTA);
                    situacaoProtestoServico.salvar(situacaoProtesto);

                    if (boleto != null) {
                        boleto = boletoServico.pesquisar(boleto.getId());
                        File file = File.createTempFile("carta-temp-protesto", ".pdf");
                        FileUtils.writeByteArrayToFile(file, boletoServico.gerarBoletoProtestoByte(boleto));

                        cartas.add(new FileInputStream(file));
                    }
                } catch (BoletoException ex) {
                    Mensagem.messagemError(ex.getMessage());
                }
            }

            RelatorioServico.gerarRelatorioUnico(cartas);
        } else if (configuracao.isNovaIntimacaoProtesto()) {
            List<FileInputStream> cartas = new ArrayList<FileInputStream>();

            Date dataAtual = new Date();
            for (Protesto protesto : protestos) {
                try {
                    if (!ProtestoUtils.protestoFinalizado(protesto)
                            && !protesto.getStatusProtesto().equals(StatusProtesto.PROTESTO_NOTIFICADO_POR_CARTA)
                            && !protesto.getStatusProtesto().equals(StatusProtesto.PROTESTO_NOTIFICADO_POR_EDITAL)) {

                        if (adicionarCustas) {
                            Ato atoConducao = atoServico.findFilter("protestoDespesasConducao", true).get(0);
                            CustaProtesto custaProtesto = new CustaProtesto();
                            custaProtesto.setDataCadastro(dataAtual);
                            custaProtesto.setUsuario(usuarioLogado);
                            custaProtesto.setValor(valorEdital);
                            custaProtesto.setAto(atoConducao);
                            custaProtesto.setDescricao("Custas de condução");
                            protesto.getCustasProtesto().add(custaProtesto);
                        }

                        protesto.setStatusProtesto(StatusProtesto.NOTIFICAO_EMITIDA_CARTA);
                        protesto.setDataAcao(dataAtual);
                        protestoServico.atualizar(protesto);
                    }

                    SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
                    situacaoProtesto.setProvimentoIntimacaoProv97(provimentoIntimacaoProv97);
                    situacaoProtesto.setDataCriacao(dataAtual);
                    situacaoProtesto.setUsuario(usuarioServico.getUserLogged());
                    situacaoProtesto.setProtesto(protesto);
                    situacaoProtesto.setTipoSituacaoProtesto(TipoSituacaoProtesto.EMITIR_CARTA);
                    situacaoProtestoServico.salvar(situacaoProtesto);


                    protesto.setProvimentoIntimacaoProv97(provimentoIntimacaoProv97);

                    File file = File.createTempFile("carta-temp-protesto", ".pdf");
                    FileUtils.writeByteArrayToFile(file, boletoServico.gerarCartaSemBoletoToByte(protesto));

                    cartas.add(new FileInputStream(file));

                } catch (com.lowagie.text.DocumentException ex) {
                    Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            RelatorioServico.gerarRelatorioUnico(cartas);
        } else {
            protestoServico.emitirIntimacaoLote(protestos, adicionarCustas, valorEdital, provimentoIntimacaoProv97);
        }
    }

    /**
     * ADICIONAR CUSTAS *
     */
    public void addDespesasSms(Protesto protesto, Date dataAtual) throws ProtestoException {

        List<Ato> atoDemaisDepesas = atoServico.findFilter("protestoDemaisDespesasAvulsa", true);
        if (!Utils.isEmpty(atoDemaisDepesas)) {
            BigDecimal valorSms = BigDecimal.ZERO;
            if (configuracao.getValorSms() != null) {
                valorSms = configuracao.getValorSms();
            }

            CustaProtesto custa = ProtestoUtils.adicionarCusta(protesto, atoDemaisDepesas, valorSms, usuarioEdital, modeloProcessoServico, new Date());
            if (custa != null) {

                custa.setDescricao("Custa de envio de SMS");
            } else {
                throw new ProtestoException("ocoreu um erro ao adicionar a custas de SMS");
            }
        } else {
            throw new ProtestoException("Não foi possível adicionar o ato correspondente a cobrança de SMS");
        }

    }

    public void intimacaoEletronica() {

        Usuario usuario = usuarioServico.getUserLogged();
        Date dataAtual = new Date();

        ModeloDocumento documentoSms = modeloDocumentoServico.getCartaProtestoSmsProv97();
        ModeloDocumento documentoEmail = modeloDocumentoServico.getCartaProtestoEmailProv97();

        String textoSms = "";
        String textoEmail = "";

        String telefone = "";
        String email = "";

        for (Protesto protesto : protestos) {

            email = "";
            telefone = "";
            try {

                for (Pessoa pessoa : protesto.getDevedores()) {

                    DocumentoAnexo cartaSms = DocumentoAnexoUtils.getInstance(documentoSms);

                    DocumentoAnexo cartaEmail = DocumentoAnexoUtils.getInstance(documentoEmail);

                    protestoServico.macrosProtesto(cartaSms, pessoa, null, protesto);
                    textoSms = Caracteres.htmlToText(cartaSms.getTexto());

                    protestoServico.macrosProtesto(cartaEmail, pessoa, null, protesto);
                    textoEmail = cartaEmail.getTexto();

                    for (Telefone tel : pessoa.getTelefones()) {

                        if (tel.getNumero() != null && !tel.getNumero().equals("000000000") && Caracteres.verificarCelular(tel.getNumero())) {
                            telefone += ",";
                            telefone += (tel.getDdd() == null ? cartorio.getTelefone().getDdd() : tel.getDdd());
                            telefone += tel.getNumero();
                        } else {
                            telefone = "";
                        }

                    }

                    if (!Utils.isEmpty(pessoa.getEmail())) {
                        email += "," + pessoa.getEmail();
                    }
                }

                email = email.replaceFirst(",", "");
                telefone = telefone.replaceFirst(",", "");

                if (!Utils.isEmpty(telefone)) {

                    boolean enviado = smsServico.enviar(configuracao, telefone, textoSms);

                    if (enviado) {
                        try {

                            addDespesasSms(protesto, new Date());

                        } catch (ProtestoException ex) {
                            Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        protesto.setDataPrazoProtesto(feriadoServico.proximoDiaUtil(new Date(), configuracao.getPrazoProtestoSmsEmail()));
                        protesto.setStatusProtesto(StatusProtesto.NOTIFICAO_EMITIDA_CARTA);
                        protesto.setDataAcao(dataAtual);
                        protestoServico.atualizar(protesto);

                        SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
                        situacaoProtesto.setProvimentoIntimacaoProv97(true);
                        situacaoProtesto.setDataCriacao(dataAtual);
                        situacaoProtesto.setUsuario(usuario);
                        situacaoProtesto.setObservacao(telefone != null ? " Telefone: " + telefone : "");

                        situacaoProtesto.setProtesto(protesto);
                        situacaoProtesto.setTipoSituacaoProtesto(TipoSituacaoProtesto.NOTIFICACAO_PROVIMENTO97_SMS);

                        situacaoProtestoServico.salvar(situacaoProtesto);

                    }
                }

                if (!Utils.isEmpty(email)) {

                    EnviarEmailVo enviarEmailVo = new EnviarEmailVo();
                    enviarEmailVo.setDestinatario(email);
                    enviarEmailVo.setAssunto("Intimação de Protesto (Prov. 97/CNJ) - " + cartorio.getNomeFantasia());
                    enviarEmailVo.setMensagem(textoEmail);

                    Email emailOb = emailServico.salvar(enviarEmailVo);

                    boolean enviado = true;

                    if (enviado) {
//                    try {
//                        addDespesasSms(protesto, new Date());
//                    } catch (ProtestoException ex) {
//                        Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
//                    }

                        protesto.setDataPrazoProtesto(feriadoServico.proximoDiaUtil(new Date(), configuracao.getPrazoProtestoSmsEmail()));
                        protesto.setStatusProtesto(StatusProtesto.NOTIFICAO_EMITIDA_CARTA);
                        protesto.setDataAcao(dataAtual);
                        protestoServico.atualizar(protesto);

                        SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
                        situacaoProtesto.setProvimentoIntimacaoProv97(true);
                        situacaoProtesto.setDataCriacao(dataAtual);
                        situacaoProtesto.setUsuario(usuario);
                        situacaoProtesto.setObservacao((!Utils.isEmpty(email) ? "E-mail: " + email.toLowerCase() : ""));

                        situacaoProtesto.setProtesto(protesto);
                        situacaoProtesto.setTipoSituacaoProtesto(TipoSituacaoProtesto.NOTIFICACAO_PROVIMENTO97_EMAIL);

                        situacaoProtestoServico.salvar(situacaoProtesto);

                        emailOb.setSituacaoProtesto(situacaoProtesto);
                        emailServico.update(emailOb);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    public void selecionarTitulosEdital() {
        this.titulosParaEdital = new ArrayList<Protesto>();
        this.titulosSelecionados = new ArrayList<Protesto>();
        if (!Utils.isEmpty(this.codigosApontamentos) || adicionarTitulosMarcados || adicionarTitulosVencidosPendentes) {
            this.titulosSelecionados = protestoServico.obterProtestosIntimacaoEdital(formatarCodigosProtesto(this.codigosApontamentos), adicionarTitulosMarcados, adicionarTitulosVencidosPendentes, statusTitulosPedentes, dataInicio, dataFim);
            if (Utils.isEmpty(this.titulosSelecionados)) {
                Mensagem.messagemWarn("Nenhum dos apontamentos citados pode ser emitidos por edital, verifique se os mesmos constam com o status 'Apontado', ou 'Notificação emitida por Carta/Edital' e não são por Fim falimentar.");
            } else {
                Mensagem.messagemInfo("Verifique os títulos selecionados e depois emita o edital.");
            }

            this.titulosParaEdital.addAll(this.titulosSelecionados);
        } else {
            Mensagem.messagemWarn("Informe os apontamentos que devem ser publicados no edital.");
        }
    }

    public void emitirEdital() {
        if (!Utils.isEmpty(this.titulosSelecionados) && !selando) {
            selando = true;
            try {
                Date dataAtual = new Date();
                AtoGenerico edital;
                if (this.configuracao.isMarcarComoAceiteQuandoEmitirEdital()) {
                    edital = protestoServico.emitirEditalIntimacao(this.titulosSelecionados, dataPrazo, dataAceite, modeloDocumentoEdital);
                    edital.setVencimentoCertidao(dataAceite);
                } else {
                    edital = protestoServico.emitirEditalIntimacao(this.titulosSelecionados, this.modeloDocumentoEdital);
                    edital.setVencimentoCertidao(new Date());
                }
                edital.getDocumentoAnexo().setTitulo("Edital de protesto criado em " + DateUtils.format(dataAtual));
                edital.setEntidade(Classe.PROTESTO);

                edital.setProtestos(this.titulosSelecionados);
                edital.setEditalProtesto(true);
                atoGenericoServico.salvar(edital);

                if (adicionarCustas) {
                    BigDecimal valorTotal = valorEdital;
                    Ato atoEdital = atoServico.findFilter("protestoEdital", true).get(0);
                    List<Ato> atosBoleto = atoServico.findFilter("taxaBoleto", true);
                    Ato atoBoleto = null;

                    if (!Utils.isEmpty(atosBoleto)) {
                        atoBoleto = atosBoleto.get(0);
                    }

                    if (!editalValorFixo) {

                        BigDecimal valorEditalDividido = new BigDecimal(valorEdital.doubleValue() / this.titulosSelecionados.size()).divide(BigDecimal.ONE).setScale(2, RoundingMode.HALF_EVEN);
                        for (Protesto protesto : this.titulosSelecionados) {
                            if (this.configuracao.isMarcarComoAceiteQuandoEmitirEdital()) {
                                if (dataPrazo == null) {
                                    protesto.setDataPrazoEdital(feriadoServico.proximoDiaUtil(new Date(), configuracaoSistemaServico.obterConfiguracao().getPrazoProtesto()));
                                } else {
                                    protesto.setDataPrazoEdital(dataPrazo);
                                }
                            }

                            if (adicionarCustas) {
                                adicionarCustasProtesto(protesto, atoEdital, valorEditalDividido, "Custa de Edital");
                                valorTotal = valorTotal.subtract(valorEditalDividido);
                            }

                            if (!Utils.isEmpty(protesto.getBoletos()) && atoBoleto != null) {
                                adicionarCustasProtesto(protesto, atoBoleto, configuracao.getValorTaxaBoleto(), "Taxa de Boleto [Edital]");
                            }
                            protestoServico.update(protesto);
                        }

                        if (adicionarCustas && this.titulosSelecionados.size() > 1) {
                            Protesto primeiroTitulo = protestoServico.pesquisar(this.titulosSelecionados.get(0).getId());
                            CustaProtesto custaEditalAtual;
                            for (int i = primeiroTitulo.getCustasProtesto().size() - 1; i > 0; i--) {
                                custaEditalAtual = primeiroTitulo.getCustasProtesto().get(i);
                                if (custaEditalAtual.getAto().isProtestoEdital()) {
                                    custaEditalAtual.setValor(valorEditalDividido.add(valorTotal));
                                    protestoServico.update(primeiroTitulo);
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Protesto protesto : this.titulosSelecionados) {
                            adicionarCustasProtesto(protesto, atoEdital, valorEdital, "Custa de Edital");
                            if (!Utils.isEmpty(protesto.getBoletos())) {
                                adicionarCustasProtesto(protesto, atoBoleto, configuracao.getValorTaxaBoleto(), "Taxa de Boleto [Edital]");
                            }
                            protestoServico.update(protesto);
                        }
                    }
                }

                for (Protesto protesto : this.titulosSelecionados) {
                    protesto = protestoServico.pesquisar(protesto.getId());

                    // remove os boletos
                    if (cartorio.isUsaBoleto()) {
                        try {

                            boletoServico.baixaBoleto(protesto);
                            Boleto boleto = boletoServico.gerarNovoBoleto(protesto, configuracao, usuarioLogado);
                            boletoServico.pesquisar(boleto.getId());

                            boletoServico.gerarBoletoProtestoByte(protestoServico.pesquisarUltimoBoleto(protesto));
                        } catch (EJBException e) {
                            Mensagem.messagemWarn("Apontamento " + protesto.getApontamento() + e.getMessage());
                        }
                    }
                }

                selando = false;
                Mensagem.messagemInfoRedirect("Edital gerado com sucesso !", "visualizarAtoGenerico.xhtml?atoGenericoId=" + edital.getId());
            } catch (Exception e) {
                if (e instanceof EJBException) {
                    Mensagem.messagemWarn(e.getMessage());
                } else {
                    System.err.println(e);
                    Mensagem.messagemError("Ocorreu um erro desconhecido");
                }
                selando = false;
            }
        } else {
            Mensagem.messagemWarn("Informe os apontamentos que devem ser publicados no edital.");
        }
    }

    public void registrarPagamentoTitulos() {
        if (!Utils.isEmpty(this.codigosApontamentos)) {
            List<Protesto> titulosNotificados = protestoServico.obterProtestosPorProtocoloSDT(formatarCodigosProtesto(this.codigosApontamentos));

            if (!Utils.isEmpty(titulosNotificados)) {
                for (Protesto protesto : titulosNotificados) {
                    ProtestoUtils.pagarTituloBoleto(protesto, protestoServico, dataAceite);
                }
                Mensagem.messagemInfo("Títulos pagos com sucesso !");
            } else {
                Mensagem.messagemWarn("Nenhum título pago.");
            }
            this.codigosApontamentos = "";
        } else {
            Mensagem.messagemWarn("É necessário informar o número dos apontamentos !");
            this.protestosNotificados = null;
        }
    }

    public void preProcessamentoRegerarBoletos() {
        if (!selando) {
            selando = true;
            if (!Utils.isEmpty(this.codigosApontamentos)) {
                List<Protesto> titulosRegerarBoletos = protestoServico.obterProtestosRegerarBoleto(formatarCodigosProtesto(this.codigosApontamentos));
                Boleto boleto;
                boletoProtestoASerCancelado = new ArrayList<Protesto>();
                if (!Utils.isEmpty(titulosRegerarBoletos)) {
                    for (Protesto protesto : titulosRegerarBoletos) {
                        boleto = protestoServico.pesquisarUltimoBoleto(protesto);


                        if (boleto != null
                                && boleto.getStatusBoleto() != null && (boleto.getStatusBoleto().equals(StatusBoleto.PEDIR_BAIXA) || boleto.getStatusBoleto().equals(StatusBoleto.BAIXA) || boleto.getStatusBoleto().equals(StatusBoleto.CANCELADO))) {
                            regerarBoleto(protesto);
                        } else if (boleto != null && boleto.getStatusRemessa() != null && (boleto.getStatusRemessa().equals(StatusRemessa.PENDENTE))) {
                            try {

                                adicionarCustasBoletoEAlterarPrazo(protesto);

                                boleto.setDataVencimento(protestoServico.obterDataVencimentoBoletoProtesto(protesto));
                                boleto.setValorTitulo(protestoServico.calcularValorBoletoProtesto(protesto));
                                boletoServico.update(boleto);

                                this.protestosEmitirIntimacao.add(0, protestoServico.load(protesto.getId()));
                                this.boletosRegerados.add(0, protestoServico.load(protesto.getId()));
                            } catch (BoletoException ex) {
                                Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (boleto != null) {
                            Protesto protestoAtualizado = protestoServico.load(protesto.getId());
                            protestoAtualizado.setConfirmarAceite(true);
                            protestoAtualizado.setBoletoTemp(boleto);
                            boletoProtestoASerCancelado.add(protestoAtualizado);
                        } else {
                            try {
                                boletoServico.gerarNovoBoleto(protesto, configuracao, usuarioLogado);
                                this.protestosEmitirIntimacao.add(0, protestoServico.load(protesto.getId()));
                                this.boletosRegerados.add(0, protestoServico.load(protesto.getId()));
                            } catch (BoletoException ex) {
                                Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (!Utils.isEmpty(boletoProtestoASerCancelado)) {
                        RequestContext.getCurrentInstance().execute("confirmarCancelamentoBoletoEGerarNovo.show();");
                    } else {
                        Mensagem.messagemInfo("Boletos regerados com sucesso !");
                        this.boletosRegerados = new ArrayList<Protesto>();
                    }
                } else {
                    Mensagem.messagemWarn("Nenhum dos apontamentos citados pode ser registrado um novo boleto, verifique se os mesmos estão aptos para regerar um boleto.");
                }
                this.codigosApontamentos = "";
            } else {
                Mensagem.messagemWarn("É necessário informar o número dos apontamentos !");
                this.protestosNotificados = null;
            }
            selando = false;
        }
    }

    public void cancelarBoletosERegerar() {
        if (!selando) {
            selando = true;
            for (Protesto protesto : boletoProtestoASerCancelado) {
                if (protesto.isConfirmarAceite()) {
                    // remove os boletos
                    if (protesto.getUsaBoleto() != null && protesto.getUsaBoleto()) {
                        boletoServico.baixaBoleto(protesto);
                        regerarBoleto(protesto);
                        this.boletosRegerados.add(0, protestoServico.load(protesto.getId()));
                    }
                }
            }
            if (!Utils.isEmpty(boletosRegerados)) {
                Mensagem.messagemInfo("Boletos regerados com sucesso !");
            } else {
                Mensagem.messagemWarn("Nenhum boleto confirmado !");
            }
            boletosRegerados = new ArrayList<Protesto>();

            this.codigosApontamentos = "";
            selando = false;
        }
    }

    private void regerarBoleto(Protesto protesto) {

        adicionarCustasBoletoEAlterarPrazo(protesto);

        try {
            boletoServico.gerarNovoBoleto(protesto, configuracao, usuarioLogado);
            this.protestosEmitirIntimacao.add(0, protestoServico.load(protesto.getId()));
        } catch (BoletoException ex) {
            Logger.getLogger(ManagerPesquisarProtesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void notificacaoTentativaLote() {
        if (!Utils.isEmpty(this.codigosApontamentos)) {
            List<Protesto> titulosNotificados = protestoServico.registrarTentativaProtestoLote(protestoServico.obterProtestosIntimados(formatarCodigosProtesto(this.codigosApontamentos)), dataAceite, usuarioNotificacao);

            if (!Utils.isEmpty(titulosNotificados)) {


                for (Protesto protesto : titulosNotificados) {
                    Protesto load = protestoServico.load(protesto.getId());
                    if (marcarEdital && motivoEditalProtesto != null) {
                        load.setMarcadoParaEdital(true);
                        load.setMotivoEditalProtesto(motivoEditalProtesto);
                        if (usuarioNotificacao != null) {
                            load.setUsuarioEdital(this.usuarioNotificacao);
                        } else {
                            load.setUsuarioEdital(usuarioLogado);
                        }
                        protestoServico.atualizar(load);
                        load = protestoServico.load(protesto.getId());
                    }

                    if (marcarDevolucao && irregularidadeProtesto != null) {
                        load.setIrregularidadesProtesto(Arrays.asList(irregularidadeProtesto));
                        if (usuarioNotificacao != null) {
                            load.setUsuarioIrregularidades(this.usuarioNotificacao);
                        } else {
                            load.setUsuarioIrregularidades(usuarioLogado);
                        }
                        ProtestoUtils.devolverTitulo(load, tipoSituacaoProtesto, protocoloItemServico, atoGenericoServico, protestoServico, protocoloServico, situacaoProtestoServico, usuarioLogado, configuracao);
                    }

                    this.protestosNotificados.add(0, load);
                }
                Mensagem.messagemInfo("Tentativa registrada com sucesso !");
            } else {
                Mensagem.messagemWarn("Nenhum dos apontamentos citados pode ser registrado uma tentativa de entrega, verifique se os mesmos estão na situação 'Notificação emitida por carta'.");
            }
            this.codigosApontamentos = "";
        } else {
            Mensagem.messagemWarn("É necessário informar o número dos apontamentos !");
            this.protestosNotificados = null;
        }
    }

    public void preProcessamentoAceiteCartas() {
        if (!Utils.isEmpty(this.codigosApontamentos)) {
            List<Protesto> titulosEncontrados = protestoServico.obterProtestosIntimados(formatarCodigosProtesto(this.codigosApontamentos));
            this.protestos = new ArrayList<Protesto>();
            this.protestosFimFalimentar = new ArrayList<Protesto>();

            if (!Utils.isEmpty(titulosEncontrados)) {
                for (Protesto protesto : titulosEncontrados) {
                    this.protestos.add(protesto);
                    if (protesto.isFimFalimentar()) {
                        this.protestosFimFalimentar.add(protesto);
                    }
                }

                if (Utils.isEmpty(protestosFimFalimentar)) {
                    notificacaoAceitaLote();
                }
            } else {
                Mensagem.messagemWarn("Nenhum dos apontamentos citados pode ser notificado, verifique se os mesmos já possuem data de aceite.");
            }
            this.codigosApontamentos = "";
        } else {
            Mensagem.messagemWarn("É necessário informar o número dos apontamentos !");
            this.protestosNotificados = null;
        }
    }

    public void setarRecebedorNosTitulosFalimentares() {
        notificacaoAceitaLote();
    }

    public void cancelarAceite() {
        this.protestos = new ArrayList<Protesto>();
        this.protestosFimFalimentar = new ArrayList<Protesto>();
    }

    public void notificacaoAceitaLote() {
        if (!Utils.isEmpty(this.protestos)) {
            this.protestos = protestoServico.notificarProtestoLote(this.protestos, dataAceite, dataPrazo, usuarioNotificacao);
            for (Protesto protesto : this.protestos) {
                if (protesto.isIntimacaoPorEdital()) {
                    protesto.setIntimacaoEdital(dataAceite);
                } else {
                    protesto.setIntimacaoPessoal(dataAceite);
                }
                protesto.setDataAcao(dataAceite);

                protestoServico.update(protesto);
                this.protestosNotificados.add(0, protestoServico.load(protesto.getId()));
            }
            Mensagem.messagemInfo("Títulos notificados com sucesso !");
            this.codigosApontamentos = "";
            this.protestos = null;
            this.protestosFimFalimentar = null;
        } else {
            Mensagem.messagemWarn("É necessário informar o número dos apontamentos !");
            this.protestosNotificados = null;
        }
    }

    public boolean renderizarDataTableLivro(ProtocoloItem item) {

        boolean renderizar = true;

        if (item.getLivro() != null) {

            if (!Caracteres.removeCaracteresEspeciais(item.getLivro().repr()).trim().isEmpty() || item.getLivro().getFolhas() != null) {
                renderizar = false;
            }
        }

        if (Utils.isEmpty(item.getSelos()) && renderizar) {
            renderizar = false;
        }

        return renderizar;
    }

    public void cancelarNotificacao() {
        protestoServico.cancelarNotificacao(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Entrega do título cancelada com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void cancelarProtesto() {
        protestoServico.cancelarProtesto(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Protesto do título cancelado com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void cancelarSuspensaoProtesto() {
        protestoServico.cancelarSuspensaoProtesto(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Suspensão do protesto cancelada com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void cancelarPagamento() {
        protestoServico.cancelarPagamento(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Pagamento do título cancelado com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void cancelarDevolucao() {
        protestoServico.cancelarDevolucao(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Devolução do título cancelado com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void cancelarRetirada() {
        protestoServico.cancelarRetirada(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Retirada do título cancelada com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void cancelarBaixa() {
        protestoServico.cancelarBaixa(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Baixa do título cancelada com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());

    }

    public void editarSituacao() {
        situacaoProtestoServico.update(spEditar);
        Mensagem.messagemInfoRedirect("Situação editada com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void baixarTituloEmNomeDoApresentante() {
        ProtestoUtils.cancelarProtestoPorArquivoCP(protestoVisualizar, protocoloItemServico, atoGenericoServico, protestoServico, protocoloServico, situacaoProtestoServico, usuarioLogado, configuracao);
        Mensagem.messagemInfoRedirect("Título cancelado com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void suspenderProtesto() {
        ProtestoUtils.suspenderProtesto(protestoVisualizar, motivoSuspensaoProtesto, descricaoMotivoSuspensao, protestoServico, situacaoProtestoServico, usuarioLogado);
        Mensagem.messagemInfoRedirect("Título suspenso com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void solucionarSuspencaoProtesto() {
        ProtestoUtils.solucionarSuspencaoProtesto(protestoVisualizar, tipoSolucaoSuspensaoProtesto, descricaoSolucaoSuspensao, protestoServico, situacaoProtestoServico, usuarioLogado);
        Mensagem.messagemInfoRedirect("Título solucionado com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void removerSituacao() {
        spRemocao.setActive(false);
        spRemocao.setProtesto(null);
        situacoesProtesto.remove(spRemocao);
        situacaoProtestoServico.update(spRemocao);
        Mensagem.messagemInfoRedirect("Situação removida com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void alterarDataIntimacao() {
        protestoServico.update(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Data de intimação atualizada com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }
    
    public void alterarDataPublicacao() {
        protestoServico.update(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Data da publicação atualizada com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
        System.out.println(protestoVisualizar.getIntimacaoEdital());
    }

    private StreamedContent download(File file) throws FileNotFoundException {
        InputStream stream = new FileInputStream(file);

        String nome = file.getName();
        DefaultStreamedContent defaultStreamedContent = new DefaultStreamedContent(stream, "text/plain;charset=utf-8", nome);
        defaultStreamedContent.setContentEncoding("UTF-8 with BOM");
        return defaultStreamedContent;
    }

    private StreamedContent downloadZip(File file) throws FileNotFoundException {
        InputStream stream = new FileInputStream(file);

        String nome = file.getName();
        DefaultStreamedContent defaultStreamedContent = new DefaultStreamedContent(stream, "application/zip", nome);
        return defaultStreamedContent;
    }

    public void onBlurNumeroRemessa() {
        if (central == 1) {

            if (numeroRemessa == null || numeroRemessa > protestoServico.ultimoNumeroArquivoSerasa() + 1) {
                numeroRemessa = protestoServico.ultimoNumeroArquivoSerasa() + 1;
                Mensagem.messagemWarn("O número da remessa deve seguir a sequencial, ou ser menor ou igual ao número da ultima remessa enviada.");
            }
        } else {
            if (numeroRemessa == null || numeroRemessa > protestoServico.ultimoNumeroArquivoBoaVista() + 1) {
                numeroRemessa = protestoServico.ultimoNumeroArquivoBoaVista() + 1;
                Mensagem.messagemWarn("O número da remessa deve seguir a sequencial, ou ser menor ou igual ao número da ultima remessa enviada.");
            }
        }
    }

    public StreamedContent gerarArquivoIEPTB() throws IOException {
        File arquivo = this.protestoServico.gerarArquivoCentralIEPTB(this.dataInicio, this.dataFim, this.titulosPesquisa, cargaInicial);
        StreamedContent download;
        if (arquivo.getName().contains(".zip")) {
            download = downloadZip(arquivo);
        } else {
            download = download(arquivo);
        }
        return download;
    }

    public void processarArquivoSerasa() throws IOException {
        try {
            if (central == 1) {
                if (numeroRemessa != null && numeroRemessa <= protestoServico.ultimoNumeroArquivoSerasa()) {
                    download = download(this.protestoServico.gerarArquivoCentral(this.situacoesProtesto, numeroRemessa, central, configuracao.getCodigoCartorioBoaVista()));
                } else {
                    download = download(this.protestoServico.gerarArquivoCentral(this.situacoesProtesto, null, central, configuracao.getCodigoCartorioBoaVista()));
                }
            } else {
                if (numeroRemessa != null && numeroRemessa <= protestoServico.ultimoNumeroArquivoBoaVista()) {
                    download = download(this.protestoServico.gerarArquivoCentral(this.situacoesProtesto, numeroRemessa, central, configuracao.getCodigoCartorioBoaVista()));
                } else {
                    download = download(this.protestoServico.gerarArquivoCentral(this.situacoesProtesto, null, central, configuracao.getCodigoCartorioBoaVista()));
                }
            }
            RequestContext.getCurrentInstance().execute("btnGerarArquivo.jq.click()");
        } catch (ProtestoException ex) {
            Mensagem.messagemWarn(ex.getMessage());
        } catch (Exception e) {
            Mensagem.messagemWarn(Mensagem.Failure);
            Logger.getLogger(Mensagem.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public StreamedContent gerarArquivoSerasa() throws IOException {
        return download;
    }

    public void pesquisarTitulosApontados() {

        this.protestos = protestoServico.pesquisarTitulosApontados(dataInicio, dataFim);
    }

    public void pesquisarTitulosSerasa() {
        if (titulosPesquisa == null) {
            this.situacoesProtesto = protestoServico.titulosProtestadosPorPeriodoEBaixados(dataInicio, dataFim);
        } else if (titulosPesquisa == 1) {
            this.situacoesProtesto = protestoServico.titulosProtestadosPorPeriodo(dataInicio, dataFim);
        } else if (titulosPesquisa == 2) {
            this.situacoesProtesto = protestoServico.titulosBaixadosPorPeriodo(dataInicio, dataFim);
        }
    }

    public void pesquisarTitulosPagosIndevidamente() {
        this.protestos = protestoServico.titulosPagosIndevidamentePorPeriodo(dataInicio, dataFim);
        if (!Utils.isEmpty(this.protestos)) {
            RequestContext.getCurrentInstance().execute("buttonGerarRelatorio.jq.click();");
        } else {
            Mensagem.messagemWarn("Nenhum título encontrado.");
        }
    }

    public void gerarRelatorioTitulosPagosIndevidamente() {
        protestoServico.gerarRelatorioTitulosProtestados(dataInicio, dataFim, null, TipoRelatorioProtesto.PAGAMENTO_INDEVIDO, protestos);
    }

    public void pesquisarTitulosProtestados() {
        this.apresentanteTitulo = new ArrayList<Map<String, Object>>();
        if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_PROTESTADOS) {
            this.protestos = protestoServico.titulosProtestadosPorPeriodo(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), tipoDeConsultaDeTitulos);
        } else if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_RETIRADOS) {
            this.protestos = protestoServico.titulosRetiradosPorPeriodo(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), tipoDeConsultaDeTitulos);
        } else if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_DEVOLVIDOS) {
            this.protestos = protestoServico.titulosDevolvidosPorPeriodo(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), tipoDeConsultaDeTitulos);
        } else if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_BAIXADOS) {
            this.situacoesProtesto = protestoServico.titulosBaixadosPorPeriodo(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), tipoDeConsultaDeTitulos, tipoRelatorioBaixados);
        } else if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_PAGOS) {
            this.protestos = protestoServico.titulosPagosPorPeriodo(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), tipoDeConsultaDeTitulos);

            Map<Long, List<Map<String, Object>>> titulosPorApresentante = new HashMap<Long, List<Map<String, Object>>>();
            for (Protesto titulo : protestos) {
                // Agrupa titulos pagos por apresentantes
                if (titulo.getStatusProtesto().equals(StatusProtesto.PROTESTO_PAGO)) {
                    Pessoa apresentante = titulo.getApresentante();
                    if (titulosPorApresentante.containsKey(apresentante.getId())) {
                        Map<String, Object> map = titulosPorApresentante.get(apresentante.getId()).get(0);
                        ((List) map.get("titulos")).add(titulo);
                        map.put("quantidadeTitulos", ((List) map.get("titulos")).size());
                        if (titulo.getApresentante().isApresentanteConselho()) {
                            map.put("totalProtestos", ((BigDecimal) map.get("totalProtestos")).add(titulo.getValorAProtestar().add(titulo.getTaxaConselho())));
                        } else {
                            map.put("totalProtestos", ((BigDecimal) map.get("totalProtestos")).add(titulo.getValorAProtestar()));
                        }
                        map.put("totalDespesas", ((BigDecimal) map.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    } else {
                        List<Map<String, Object>> item = new ArrayList<Map<String, Object>>();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("apresentante", apresentante.getNome());
                        List<Protesto> protestos = new ArrayList<Protesto>();
                        protestos.add(titulo);
                        map.put("titulos", protestos);
                        map.put("quantidadeTitulos", 1);
                        if (titulo.getApresentante().isApresentanteConselho()) {
                            map.put("totalProtestos", titulo.getValorAProtestar().add(titulo.getTaxaConselho()));
                        } else {
                            map.put("totalProtestos", titulo.getValorAProtestar());
                        }
                        map.put("totalDespesas", titulo.getTotalDespesas());

                        if (!Utils.isEmpty(apresentante.getContasBancarias())) {
                            StringBuilder builder = new StringBuilder();
                            for (ContaBancaria contaBancaria : apresentante.getContasBancarias()) {
                                builder.append(contaBancaria.getBanco().getCod()).append(" - ").append(contaBancaria.getBanco().getNome());
                                builder.append("\nAgência : ").append(contaBancaria.getAgencia());
                                builder.append("\nConta ").append(contaBancaria.getTipoContaBancaria().getNome()).append(" número :").append(contaBancaria.getConta());
                                if (!Utils.isEmpty(contaBancaria.getOperacao())) {
                                    builder.append("\nOperação : ").append(contaBancaria.getOperacao());
                                }
                                break;
                            }
                            map.put("dadosBancarios", builder.toString());
                        } else {
                            map.put("dadosBancarios", null);
                        }
                        item.add(map);
                        apresentanteTitulo.add(map);
                        titulosPorApresentante.put(apresentante.getId(), item);
                    }
                }
            }
        } else if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_POR_OCORRENCIA) {
            if (Utils.isEmpty(this.tipoSituacaoProtestoRelatorio)) {
                Mensagem.messagemWarn("Informe uma ocorrência para gerar o relatório.");
                return;
            }
            List<SituacaoProtesto> situacoes = protestoServico.titulosPorOcorrencia(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), tipoDeConsultaDeTitulos, especieTitulo, tipoSituacaoProtestoRelatorio);

            Map<String, Object> retirados = new HashMap<String, Object>();
            retirados.put("ocorrencia", "Retiradas");
            retirados.put("titulos", new ArrayList<Protesto>());
            retirados.put("quantidadeTitulos", 1);
            retirados.put("totalProtestos", BigDecimal.ZERO);
            retirados.put("totalDespesas", BigDecimal.ZERO);
            retirados.put("issRetido", BigDecimal.ZERO);
            retirados.put("taxaBoleto", BigDecimal.ZERO);

            Map<String, Object> devolvidos = new HashMap<String, Object>();
            devolvidos.put("ocorrencia", "Devolvidos com custas");
            devolvidos.put("titulos", new ArrayList<Protesto>());
            devolvidos.put("quantidadeTitulos", 1);
            devolvidos.put("totalProtestos", BigDecimal.ZERO);
            devolvidos.put("totalDespesas", BigDecimal.ZERO);
            devolvidos.put("issRetido", BigDecimal.ZERO);
            devolvidos.put("taxaBoleto", BigDecimal.ZERO);

            Map<String, Object> devolvidosSemCustas = new HashMap<String, Object>();
            devolvidosSemCustas.put("ocorrencia", "Devolvidos sem custas");
            devolvidosSemCustas.put("titulos", new ArrayList<Protesto>());
            devolvidosSemCustas.put("quantidadeTitulos", 1);
            devolvidosSemCustas.put("totalProtestos", BigDecimal.ZERO);
            devolvidosSemCustas.put("totalDespesas", BigDecimal.ZERO);
            devolvidosSemCustas.put("issRetido", BigDecimal.ZERO);
            devolvidosSemCustas.put("taxaBoleto", BigDecimal.ZERO);

            Map<String, Object> pagos = new HashMap<String, Object>();
            pagos.put("ocorrencia", "Pagos");
            pagos.put("titulos", new ArrayList<Protesto>());
            pagos.put("quantidadeTitulos", 1);
            pagos.put("totalProtestos", BigDecimal.ZERO);
            pagos.put("totalDespesas", BigDecimal.ZERO);
            pagos.put("issRetido", BigDecimal.ZERO);
            pagos.put("taxaBoleto", BigDecimal.ZERO);

            Map<String, Object> protestados = new HashMap<String, Object>();
            protestados.put("ocorrencia", "Protestados");
            protestados.put("titulos", new ArrayList<Protesto>());
            protestados.put("quantidadeTitulos", 1);
            protestados.put("totalProtestos", BigDecimal.ZERO);
            protestados.put("totalDespesas", BigDecimal.ZERO);
            protestados.put("issRetido", BigDecimal.ZERO);
            protestados.put("taxaBoleto", BigDecimal.ZERO);

            Map<String, Object> baixados = new HashMap<String, Object>();
            baixados.put("ocorrencia", "Baixados");
            baixados.put("titulos", new ArrayList<Protesto>());
            baixados.put("quantidadeTitulos", 1);
            baixados.put("totalProtestos", BigDecimal.ZERO);
            baixados.put("totalDespesas", BigDecimal.ZERO);
            baixados.put("issRetido", BigDecimal.ZERO);
            baixados.put("taxaBoleto", BigDecimal.ZERO);


            this.protestos = new ArrayList<Protesto>();
            for (SituacaoProtesto situacao : situacoes) {
                Protesto titulo = situacao.getProtesto();
                this.protestos.add(titulo);
                // Agrupa titulos pagos por apresentantes
                if (situacao.getTipoSituacaoProtesto().equals(TipoSituacaoProtesto.PAGO)) {

                    ((List) pagos.get("titulos")).add(titulo);
                    pagos.put("quantidadeTitulos", ((List) pagos.get("titulos")).size());
                    if (titulo.getApresentante().isApresentanteConselho()) {
                        pagos.put("totalProtestos", ((BigDecimal) pagos.get("totalProtestos")).add(titulo.getValorAProtestar().add(titulo.getTaxaConselho())));
                    } else {
                        pagos.put("totalProtestos", ((BigDecimal) pagos.get("totalProtestos")).add(titulo.getValorAProtestar()));
                    }
                    pagos.put("totalDespesas", ((BigDecimal) pagos.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    pagos.put("issRetido", ((BigDecimal) pagos.get("issRetido")).add(titulo.getTotalIssRetido()));
                    pagos.put("taxaBoleto", ((BigDecimal) pagos.get("taxaBoleto")).add(titulo.getTaxaBoleto()));

                } else if (situacao.getTipoSituacaoProtesto().equals(TipoSituacaoProtesto.PROTESTADO)) {

                    ((List) protestados.get("titulos")).add(titulo);
                    protestados.put("quantidadeTitulos", ((List) protestados.get("titulos")).size());
                    protestados.put("totalProtestos", ((BigDecimal) protestados.get("totalProtestos")).add(titulo.getValorAProtestar()));
                    protestados.put("totalDespesas", ((BigDecimal) protestados.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    protestados.put("issRetido", ((BigDecimal) protestados.get("issRetido")).add(titulo.getTotalIssRetido()));
                } else if (situacao.getTipoSituacaoProtesto().equals(TipoSituacaoProtesto.RETIRADO)) {

                    ((List) retirados.get("titulos")).add(titulo);
                    retirados.put("quantidadeTitulos", ((List) retirados.get("titulos")).size());
                    retirados.put("totalProtestos", ((BigDecimal) retirados.get("totalProtestos")).add(titulo.getValorAProtestar()));
                    retirados.put("totalDespesas", ((BigDecimal) retirados.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    retirados.put("issRetido", ((BigDecimal) retirados.get("issRetido")).add(titulo.getTotalIssRetido()));
                } else if (situacao.getTipoSituacaoProtesto().equals(TipoSituacaoProtesto.DEVOLVIDO_COM_CUSTAS)) {

                    ((List) devolvidos.get("titulos")).add(titulo);
                    devolvidos.put("quantidadeTitulos", ((List) devolvidos.get("titulos")).size());
                    devolvidos.put("totalProtestos", ((BigDecimal) devolvidos.get("totalProtestos")).add(titulo.getValorAProtestar()));
                    devolvidos.put("totalDespesas", ((BigDecimal) devolvidos.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    devolvidos.put("issRetido", ((BigDecimal) devolvidos.get("issRetido")).add(titulo.getTotalIssRetido()));
                } else if (situacao.getTipoSituacaoProtesto().equals(TipoSituacaoProtesto.DEVOLVIDO_SEM_CUSTAS)) {
                    ((List) devolvidosSemCustas.get("titulos")).add(titulo);
                    devolvidosSemCustas.put("quantidadeTitulos", ((List) devolvidosSemCustas.get("titulos")).size());
                    devolvidosSemCustas.put("totalProtestos", ((BigDecimal) devolvidosSemCustas.get("totalProtestos")).add(titulo.getValorAProtestar()));
                    devolvidosSemCustas.put("totalDespesas", ((BigDecimal) devolvidosSemCustas.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    devolvidosSemCustas.put("issRetido", ((BigDecimal) devolvidosSemCustas.get("issRetido")).add(titulo.getTotalIssRetido()));
                } else if (situacao.getTipoSituacaoProtesto().equals(TipoSituacaoProtesto.PROTESTO_DO_BANCO_CANCELADO)) {
                    ((List) baixados.get("titulos")).add(titulo);
                    baixados.put("quantidadeTitulos", ((List) baixados.get("titulos")).size());
                    baixados.put("totalProtestos", ((BigDecimal) baixados.get("totalProtestos")).add(titulo.getValorAProtestar()));
                    baixados.put("totalDespesas", ((BigDecimal) baixados.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    baixados.put("issRetido", ((BigDecimal) baixados.get("issRetido")).add(titulo.getTotalIssRetido()));
                }

            }
            titulosPorOcorrencia = new ArrayList<Map<String, Object>>();
            if (((List) pagos.get("titulos")).size() > 0) {
                titulosPorOcorrencia.add(pagos);
            }
            if (((List) protestados.get("titulos")).size() > 0) {
                titulosPorOcorrencia.add(protestados);
            }
            if (((List) retirados.get("titulos")).size() > 0) {
                titulosPorOcorrencia.add(retirados);
            }
            if (((List) devolvidos.get("titulos")).size() > 0) {
                titulosPorOcorrencia.add(devolvidos);
            }
            if (((List) devolvidosSemCustas.get("titulos")).size() > 0) {
                titulosPorOcorrencia.add(devolvidosSemCustas);
            }
            if (((List) baixados.get("titulos")).size() > 0) {
                titulosPorOcorrencia.add(baixados);
            }

        } else if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_DISTRIBUIDOS) {
            List<SituacaoProtesto> situacoes = protestoServico.titulosDistribuicao(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), protestoPesquisa.getCartorioDistribuicao(), tipoDeConsultaDeTitulos);

            Map<String, Object> importados = new HashMap<String, Object>();
            importados.put("ocorrencia", "Importados");
            importados.put("titulos", new ArrayList<Protesto>());
            importados.put("quantidadeTitulos", 1);
            importados.put("totalProtestos", BigDecimal.ZERO);
            importados.put("totalDespesas", BigDecimal.ZERO);
            importados.put("issRetido", BigDecimal.ZERO);
            importados.put("taxaBoleto", BigDecimal.ZERO);

            Map<String, Object> particulares = new HashMap<String, Object>();
            particulares.put("ocorrencia", "Particulares");
            particulares.put("titulos", new ArrayList<Protesto>());
            particulares.put("quantidadeTitulos", 1);
            particulares.put("totalProtestos", BigDecimal.ZERO);
            particulares.put("totalDespesas", BigDecimal.ZERO);
            particulares.put("issRetido", BigDecimal.ZERO);
            particulares.put("taxaBoleto", BigDecimal.ZERO);

            this.protestos = new ArrayList<Protesto>();
            for (SituacaoProtesto situacao : situacoes) {
                Protesto titulo = situacao.getProtesto();
                this.protestos.add(titulo);
                // Agrupa titulos pagos por apresentantes
                if (situacao.getTipoSituacaoProtesto().equals(TipoSituacaoProtesto.IMPORTADO_DISTRIBUICAO)) {
                    ((List) importados.get("titulos")).add(titulo);
                    importados.put("quantidadeTitulos", ((List) importados.get("titulos")).size());
                    importados.put("totalProtestos", ((BigDecimal) importados.get("totalProtestos")).add(titulo.getValorAProtestar()));
                    importados.put("totalDespesas", ((BigDecimal) importados.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    importados.put("issRetido", ((BigDecimal) importados.get("issRetido")).add(titulo.getTotalIssRetido()));
                } else if (situacao.getTipoSituacaoProtesto().equals(TipoSituacaoProtesto.CADASTRADO_DISTRIBUICAO_PARTICULAR)) {
                    ((List) particulares.get("titulos")).add(titulo);
                    particulares.put("quantidadeTitulos", ((List) particulares.get("titulos")).size());
                    particulares.put("totalProtestos", ((BigDecimal) particulares.get("totalProtestos")).add(titulo.getValorAProtestar()));
                    particulares.put("totalDespesas", ((BigDecimal) particulares.get("totalDespesas")).add(titulo.getTotalDespesas()));
                    particulares.put("issRetido", ((BigDecimal) particulares.get("issRetido")).add(titulo.getTotalIssRetido()));
                }
            }
            titulosPorOcorrencia = new ArrayList<Map<String, Object>>();

            if (((List) importados.get("titulos")).size() > 0) {
                titulosPorOcorrencia.add(importados);
            }


            if (((List) importados.get("titulos")).size() > 0) {
                titulosPorOcorrencia.add(particulares);
            }
        }

        if (!Utils.isEmpty(this.protestos)) {
            if (this.tipoRelatorioProtestados == 3L) {
                RequestContext.getCurrentInstance().execute("buttonDownloadInstrumentos.jq.click();");
            } else {
                RequestContext.getCurrentInstance().execute("buttonGerarRelatorio.jq.click();");
            }

        } else {
            if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_PROTESTADOS) {
                Mensagem.messagemWarn("Nenhum título protestado.");

            } else if (this.tipoRelatorio == TipoRelatorioProtesto.TITULOS_BAIXADOS && !Utils.isEmpty(this.situacoesProtesto)) {
                RequestContext.getCurrentInstance().execute("buttonGerarRelatorio.jq.click();");
            } else {

                Mensagem.messagemWarn("Nenhum título encontrado.");
            }
        }
    }

    public void preProcessamentoRelatorioRelacaoSerasa() {
        if (apresentante == null || apresentante.getId() == null) {
            Mensagem.messagemWarn("Selecione o solicitante.");
        } else if (modeloDocumentoServico.getCertidaoCentralProtesto() == null) {
            Mensagem.messagemWarn("Modelo de documento não encontrado, por favor contacte o suporte.");
        } else {
            RequestContext.getCurrentInstance().execute("buttonGerarRelatorioSelado.jq.click();");

        }

    }

    public void gerarRelatorioRelacaoSerasa() {


        AtoGenerico atoGenerico = protestoServico.emitirCertidaoCentralProtesto(apresentante, dataInicio, dataFim, null, situacoesProtesto, usuarioAssinatura);
        atoGenericoServico.salvar(atoGenerico);
        Mensagem.messagemInfoRedirect("Certidão gerada, vincule o processo.", "criarAtoGenerico.xhtml?atoGenericoId=" + atoGenerico.getId());
    }

    public void gerarRelatorioCentraisDeProtesto() {

        protestoServico.gerarRelatorioCentraisDeProtesto(dataInicio, dataFim, situacoesProtesto, titulosPesquisa);
    }

    public void gerarRelatorioTitulosProtestadosSerasa() {

        protestoServico.gerarRelatorioTitulosSerasa(dataInicio, dataFim, situacoesProtesto, numeroRemessa.intValue(), tituloSerasa);
    }

    public void gerarRelatorioTitulosProtestadosIEPTB() {

        protestoServico.gerarRelatorioTitulosCNP(dataInicio, dataFim, situacoesProtesto);
    }

    public void gerarRelatorioTitulosProtestados() throws FileNotFoundException, IOException, DocumentException {
        if (tipoRelatorio == TipoRelatorioProtesto.TITULOS_PROTESTADOS && tipoRelatorioProtestados == 1L) {
            Map<Pessoa, List<Protesto>> protestosApresentante = new HashMap<Pessoa, List<Protesto>>();
            for (Protesto protesto : protestos) {
                List<Protesto> lista = protestosApresentante.get(protesto.getApresentante());
                if (lista == null) {
                    lista = new ArrayList<Protesto>();
                    lista.add(protesto);
                    protestosApresentante.put(protesto.getApresentante(), lista);
                } else {
                    lista.add(protesto);
                }
            }

            List<FileInputStream> inputStream = new ArrayList<FileInputStream>();
            for (Map.Entry<Pessoa, List<Protesto>> item : protestosApresentante.entrySet()) {
                Pessoa pessoa = item.getKey();
                List<Protesto> list = item.getValue();
                File capa = protestoServico.gerarRelatorioTitulosProtestados(dataInicio, dataFim, pessoa, tipoRelatorio, list, true);
                inputStream.add(new FileInputStream(capa));
                for (Protesto protesto : list) {
                    inputStream.add(new FileInputStream(protestoServico.emitirInstrumento(protesto, true, false)));
                }
            }

            RelatorioServico.gerarRelatorioUnico(inputStream);
        } else if (tipoRelatorio == TipoRelatorioProtesto.TITULOS_DEVOLVIDOS || tipoRelatorio == TipoRelatorioProtesto.TITULOS_RETIRADOS) {
            Map<Pessoa, List<Protesto>> protestosApresentante = new HashMap<Pessoa, List<Protesto>>();
            for (Protesto protesto : protestos) {
                List<Protesto> lista = protestosApresentante.get(protesto.getApresentante());
                if (lista == null) {
                    lista = new ArrayList<Protesto>();
                    lista.add(protesto);
                    protesto.setValorApontamentoTemp(protestoServico.criarProcessoApontamento72Hrs(protesto).getValorCartorioComDesconto());
                    protestosApresentante.put(protesto.getApresentante(), lista);
                } else {
                    lista.add(protesto);
                }
            }

            List<FileInputStream> inputStream = new ArrayList<FileInputStream>();
            for (Map.Entry<Pessoa, List<Protesto>> item : protestosApresentante.entrySet()) {
                Pessoa pessoa = item.getKey();
                List<Protesto> list = item.getValue();
                File capa = protestoServico.gerarRelatorioTitulosProtestados(dataInicio, dataFim, pessoa, tipoRelatorio, list, true);
                inputStream.add(new FileInputStream(capa));
            }

            RelatorioServico.gerarRelatorioUnico(inputStream);
        } else if (tipoRelatorio == TipoRelatorioProtesto.TITULOS_POR_OCORRENCIA) {
            protestoServico.gerarRelatorioTitulosPorOcorrencia(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), titulosPorOcorrencia, this.especieTitulo);
        } else if (tipoRelatorio == TipoRelatorioProtesto.TITULOS_DISTRIBUIDOS) {
            protestoServico.gerarRelatorioTitulosDistribuicao(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), titulosPorOcorrencia);
        } else if (tipoRelatorio == TipoRelatorioProtesto.TITULOS_BAIXADOS) {
            this.protestos = new ArrayList<Protesto>();
            for (SituacaoProtesto situacaoProtesto : situacoesProtesto) {
                final Protesto protesto = situacaoProtesto.getProtesto();
                if (!protestos.contains(protesto)) {
                    protesto.setSomarBaixaNasDespesas(true);
                    protestos.add(protesto);
                }
            }
            protestoServico.gerarRelatorioTitulosProtestados(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), tipoRelatorio, protestos);
        } else if (tipoRelatorio == TipoRelatorioProtesto.TITULOS_PAGOS) {
            protestoServico.gerarRelatorioTitulosProtestados(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), apresentanteTitulo);
        } else {
            protestoServico.gerarRelatorioTitulosProtestados(dataInicio, dataFim, this.protestoPesquisa.getApresentante(), tipoRelatorio, protestos);
        }
    }

    public StreamedContent gerarArquivosZip() throws FileNotFoundException, IOException {
        List<File> arquivos = protestoServico.emitirInstrumento(protestos, true, true);
        File zipFile = br.com.foxinline.utilitario.FileUtils.zipArchives(arquivos, "I000_" + DateUtils.format(new Date(), "ddMM.yyyy") + ".01.zip");

        return downloadZip(zipFile);
    }

    public List<EspecieTitulo> autocomplete(String str) {
        return especieTituloServico.autocomplete(str);
    }

    public List<TipoSituacaoProtesto> ocorrencias() {
        List<TipoSituacaoProtesto> lista = new ArrayList<TipoSituacaoProtesto>();
        lista.add(TipoSituacaoProtesto.PAGO);
        lista.add(TipoSituacaoProtesto.RETIRADO);
        lista.add(TipoSituacaoProtesto.DEVOLVIDO_COM_CUSTAS);
        lista.add(TipoSituacaoProtesto.DEVOLVIDO_SEM_CUSTAS);
        lista.add(TipoSituacaoProtesto.PROTESTADO);
        lista.add(TipoSituacaoProtesto.PROTESTO_DO_BANCO_CANCELADO);

        return lista;
    }

    public String itemDescricao(ProtocoloItem item) {
        return ProtocoloItemHelper.itemDescricao(item);
    }

    public Pessoa getInteressado() {
        return interessado;
    }

    public void setInteressado(Pessoa interessado) {
        this.interessado = interessado;
    }

    public Protesto getProtesto() {
        return protestoPesquisa;
    }

    public String mascaraNumero(Double numero) {
        if (numero == null) {
            return "R$ 0,00";
        } else {
            return String.format("R$ %.2f", numero);
        }

    }

    public void emitirCertidao() {
        String url = "criarAtoGenerico.xhtml?";
        url += "certidaoProtesto=" + protestoVisualizar.getId();
        List<ProtocoloItem> selos = new ArrayList<ProtocoloItem>();
        if (utilizarProcessoApontamento) {
            selos.addAll(protestoVisualizar.getApontamento().getProtocoloItems());
        }
        if (utilizarProcessoRetirada) {
            selos.addAll(protestoVisualizar.getRetirada().getProtocoloItems());
        }
        if (utilizarProcessoDevolucao) {
            selos.addAll(protestoVisualizar.getDevolucao().getProtocoloItems());
        }
        if (utilizarProcessoProtesto) {
            selos.addAll(protestoVisualizar.getProtesto().getProtocoloItems());
        }
        if (utilizarProcessoBaixa) {
            selos.addAll(protestoVisualizar.getBaixa().getProtocoloItems());
        }

        if (!Utils.isEmpty(selos)) {
            for (ProtocoloItem protocoloItem : selos) {
                url += "," + protocoloItem.getId();
            }
            url = url.replaceFirst(",", "&protocoloItens=");
        }

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect(url);


        } catch (IOException ex) {
            Logger.getLogger(Mensagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imprimirCertidao(AtoGenerico atoGenerico) {
        try {
            atoGenericoServico.exportToWeb(atoGenerico);
        } catch (CabecalhoException ex) {
            Mensagem.messagemWarn(ex.getMessage());
        }
    }

    public void removerCusta() {
        custaRemocao.setActive(false);
        protestoVisualizar.getCustasProtesto().remove(custaRemocao);
        protestoServico.update(protestoVisualizar);
        Mensagem.messagemInfoRedirect("Custa removida com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public boolean renderizarSeloBaixa() {
        if (renderizarSeloProtesto()) {
            return false;
        }

        if (protestoVisualizar.getBaixa() != null) {
            Protocolo protocoloP = protestoVisualizar.getBaixa();
            if (protocoloP.getStatusProtocolo().equals(StatusProtocolo.ABERTO) || protocoloP.getStatusProtocolo().equals(StatusProtocolo.PARCIALMENTE_PRATICADO)) {
                return true;
            }
        }

        return false;
    }

    public void registrarPagamentoEmolumentoAntecipado() {
        if (this.controlePagamentoProtesto.getComprovante() == null) {
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("pagamentoEmolumentosParticular.show();");
            Mensagem.messagemWarn("É obrigatório anexar o comprovante");
            return;
        }
        if (!selando) {
            selando = true;

            if (this.controlePagamentoProtesto.getComprovante() != null) {
                try {
                    anexoServico.gravarAnexos(Arrays.asList(this.controlePagamentoProtesto.getComprovante()));
                } catch (IOException ex) {
                    Mensagem.messagemError("Não foi possível gravar os anexos!");
                }
            }

            controlePagamentoProtesto.setDataAntecipacao(new Date());
            controlePagamentoProtesto.setUsuario(usuarioServico.getUsuarioLogado());
            controlePagamentoProtesto.setProtesto(protestoVisualizar);
            controlePagamentoProtestoServico.salvar(controlePagamentoProtesto);
            Mensagem.messagemInfoRedirect("Pagamento registrado com sucesso.", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
        }
    }

    public void registrarPagamentoTitulo() {
        ProtestoUtils.registrarPagamentoTitulo(protestoVisualizar, protestoServico, new Date());
        Mensagem.redirect("pagamentoProtocolo.xhtml?protocoloId=" + protestoVisualizar.getApontamento().getId() + "&protestoId=" + protestoVisualizar.getId());

    }

    public void desmarcarParaEdital() {
        protestoVisualizar.setMarcadoParaEdital(false);
        protestoVisualizar.setDataMarcadoParaEdital(null);
        protestoVisualizar.setUsuarioEdital(null);
        protestoVisualizar.setMotivoEditalProtesto(null);
        protestoServico.update(protestoVisualizar);

        SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
        situacaoProtesto.setDataCriacao(new Date());
        situacaoProtesto.setUsuario(usuarioServico.getUsuarioLogado());
        situacaoProtesto.setObservacao("Titulo removido do edital");
        situacaoProtesto.setProtesto(protestoVisualizar);
        situacaoProtesto.setActive(true);
        situacaoProtestoServico.salvar(situacaoProtesto);
        Mensagem.messagemInfoRedirect("Titulo retirado do edital", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void marcarParaEdital() {
        protestoVisualizar.setMarcadoParaEdital(true);
        protestoVisualizar.setDataMarcadoParaEdital(new Date());
        protestoVisualizar.setUsuarioEdital(usuarioEdital != null ? usuarioEdital : usuarioServico.getUsuarioLogado());
        protestoVisualizar.setMotivoEditalProtesto(motivoEditalProtesto);
        protestoServico.update(protestoVisualizar);

        SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
        situacaoProtesto.setDataCriacao(new Date());
        situacaoProtesto.setUsuario(usuarioEdital != null ? usuarioEdital : usuarioServico.getUsuarioLogado());
        situacaoProtesto.setObservacao("Titulo marcado para edital. Motivo : " + motivoEditalProtesto.getDescricao());
        situacaoProtesto.setProtesto(protestoVisualizar);
        situacaoProtesto.setActive(true);
        situacaoProtestoServico.salvar(situacaoProtesto);
        Mensagem.messagemInfoRedirect("Titulo marcado para edital", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
    }

    public void cancelarEdital() {
        this.usuarioEdital = null;
        this.motivoEditalProtesto = null;
    }

    public void marcarComoPago() {
        if (protestoVisualizar.getApontamento() != null && protestoVisualizar.getApontamento().getStatusProtocolo().equals(StatusProtocolo.PRATICADO)) {
            protestoVisualizar.setStatusProtesto(StatusProtesto.PROTESTO_PAGO);
            protestoVisualizar.setDataAcao(new Date());

            SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
            situacaoProtesto.setDataCriacao(new Date());
            situacaoProtesto.setUsuario(usuarioServico.getUsuarioLogado());
            situacaoProtesto.setObservacao("Protesto pago. Processo nº " + protestoVisualizar.getApontamento().getCodigo());
            situacaoProtesto.setSituacaoEnvioProtesto(SituacaoEnvioProtesto.AGUARDANDO_ENVIO);
            situacaoProtesto.setSituacaoEnvioAto(SituacaoEnvioSelo.AGUARDANDO_ENVIO);

            if (Utils.isEmpty(protestoVisualizar.getSequencialTalaoCe())) {
                protestoVisualizar.setSequencialTalaoCe(seloServico.obterSequecialTalaoCE(new Date()));
            }

            situacaoProtesto.setTipoSituacaoProtesto(TipoSituacaoProtesto.PAGO);
            situacaoProtesto.setProtesto(protestoVisualizar);
            situacaoProtesto.setActive(true);


            situacaoProtestoServico.salvar(situacaoProtesto);
            ConfiguracaoSistema configuracaoSistema = this.configuracao;
            Usuario usuarioLogado = usuarioServico.getUsuarioLogado();
            try {
                // Gera a certidão pagamento
                AtoGenerico atoGenerico = new AtoGenerico();
                atoGenerico.setDocumentoAnexo(DocumentoAnexoUtils.getInstance(configuracaoSistema.getFonte()));
                atoGenerico.setDataCadastro(new Date());
                atoGenerico.setUsuarioCadastro(usuarioLogado);
                atoGenerico.setSelado(true);
                atoGenerico.setEntidade(Classe.PROTESTO);
                atoGenerico.setIdEntidade(protestoVisualizar.getId());
                atoGenericoServico.salvar(atoGenerico);
                atoGenerico.setProtocoloItems(protestoVisualizar.getApontamento().getProtocoloItems());
                atoGenerico.setPrimeiraVia(true);
                protestoServico.emitirCertidaoPagamento(protestoVisualizar, atoGenerico);
                atoGenericoServico.atualizar(atoGenerico);
                protestoVisualizar.getCertidoes().add(atoGenerico);
                // vincula os itens na certidão
                for (ProtocoloItem item : protestoVisualizar.getApontamento().getProtocoloItems()) {
                    item.setVinculado(true);
                    item.setClasse(Classe.ATO_GENERICO);
                    item.setIdVinculado(atoGenerico.getId());
                    protocoloItemServico.update(item);
                }
            } catch (Exception e) {

                for (ProtocoloItem item : protestoVisualizar.getApontamento().getProtocoloItems()) {
                    item.setVinculado(false);
                    item.setClasse(null);
                    item.setIdVinculado(null);
                    protocoloItemServico.update(item);
                }
            }

            protestoServico.update(protestoVisualizar);
            protestoServico.pesquisar(protestoVisualizar.getId());

            Mensagem.messagemInfoRedirect("Titulo pago com sucesso !", "visualizarProtesto.xhtml?protestoId=" + protestoVisualizar.getId());
        }

    }

    public boolean renderizarSeloProtesto() {
        if (protestoVisualizar.isPostergado() && protestoVisualizar.getStatusProtesto().equals(StatusProtesto.PROTESTO) && protestoVisualizar.getBaixa() == null) {
            return false;
        }
        if (protestoVisualizar.getApontamento() != null) {
            Protocolo protocoloP = protestoVisualizar.getApontamento();
            if (protocoloP.getStatusProtocolo().equals(StatusProtocolo.ABERTO) || protocoloP.getStatusProtocolo().equals(StatusProtocolo.PARCIALMENTE_PRATICADO)) {
                return true;
            }
        }

        if (protestoVisualizar.getRetirada() != null) {
            Protocolo protocoloP = protestoVisualizar.getRetirada();
            if (protocoloP.getStatusProtocolo().equals(StatusProtocolo.ABERTO) || protocoloP.getStatusProtocolo().equals(StatusProtocolo.PARCIALMENTE_PRATICADO)) {
                return true;
            }
        }

        if (protestoVisualizar.getProtesto() != null) {
            Protocolo protocoloP = protestoVisualizar.getProtesto();
            if (protocoloP.getStatusProtocolo().equals(StatusProtocolo.ABERTO) || protocoloP.getStatusProtocolo().equals(StatusProtocolo.PARCIALMENTE_PRATICADO)) {
                return true;
            }
        }

        if (protestoVisualizar.getDevolucao() != null) {
            Protocolo protocoloP = protestoVisualizar.getDevolucao();
            if (protocoloP.getStatusProtocolo().equals(StatusProtocolo.ABERTO) || protocoloP.getStatusProtocolo().equals(StatusProtocolo.PARCIALMENTE_PRATICADO)) {
                return true;
            }
        }

        return false;
    }

    public void verificarDatas() {
        if (dataFim != null && dataInicio != null) {
            if (dataInicio.after(dataFim)) {
                dataFim = dataInicio;
            }
        }
    }

    public void buscarEncerramentos() {
        if (livroApontamento != null) {
            this.encerramentos = itemLivroProtocoloServico.buscarEncerramentos(livroApontamento, dataInicio, dataFim);
        } else {
            this.encerramentos = null;
        }
    }

    public void buscarEncerramentosEProtocolos() {

        if (livroApontamento != null) {
            this.encerramentos = itemLivroProtocoloServico.buscarEncerramentos(livroApontamento, dataInicio, dataFim);

            if (!configuracao.getRelatorioApontamento().equals(RelatorioApontamento._3_POR_FOLHA)
                    && dataInicio != null
                    && dataFim != null
                    && feriadoServico.quantidadeDiasUteis(dataInicio, dataFim) > this.encerramentos.size()) {
                RequestContext.getCurrentInstance().execute("criarEncerramentoDialog.show();");
            } else if (!configuracao.getRelatorioApontamento().equals(RelatorioApontamento._3_POR_FOLHA) && dataInicio != null && Utils.isEmpty(this.encerramentos.size())) {
                RequestContext.getCurrentInstance().execute("criarEncerramentoDialog.show();");
            } else {
                RequestContext.getCurrentInstance().execute("printButton.jq.click();");
            }
        } else {
            this.encerramentos = null;
        }
    }

    public void buscarEncerramentosEProtocolosDistribuicao() {

        if (livroApontamento != null) {
            this.encerramentos = itemLivroProtocoloServico.buscarEncerramentos(livroApontamento, dataInicio, dataFim);

            if (!configuracao.getRelatorioDistribuicao().equals(RelatorioApontamento._3_POR_FOLHA)
                    && dataInicio != null
                    && dataFim != null
                    && feriadoServico.quantidadeDiasUteis(dataInicio, dataFim) > this.encerramentos.size()) {
                RequestContext.getCurrentInstance().execute("criarEncerramentoDialog.show();");
            } else if (!configuracao.getRelatorioApontamento().equals(RelatorioApontamento._3_POR_FOLHA) && dataInicio != null && Utils.isEmpty(this.encerramentos.size())) {
                RequestContext.getCurrentInstance().execute("criarEncerramentoDialog.show();");
            } else {
                RequestContext.getCurrentInstance().execute("printButton.jq.click();");
            }
        } else {
            this.encerramentos = null;
        }
    }

    public void editarEncerramentoDiario() {
        itemLivroProtocoloServico.update(encerramento);

        buscarEncerramentos();
    }

    public void excluirTodosOsEncerramentosDiario() {
        buscarEncerramentos();

        for (ItemLivroProtocolo itemLivroProtocolo : encerramentos) {
            itemLivroProtocoloServico.inactivate(itemLivroProtocolo);
        }
        buscarEncerramentos();
        Mensagem.messagemInfo("Todos os encerramentos foram excluídos.");
    }

    public void excluirEncerramentoDiario() {
        itemLivroProtocoloServico.inactivate(encerramentoDelete);

        buscarEncerramentos();
    }

    public void selecionarComprovante(FileUploadEvent event) {
        try {
            this.arquivo = event.getFile();
            this.protestoVisualizar.setInstrumentoEletronico(this.anexoServico.adicionarArquivo(this.arquivo));


        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void adicionarAnexo() {

        try {

            this.controlePagamentoProtesto.setComprovante(anexoServico.adicionarArquivo(this.arquivo));

        } catch (FileNotFoundException e) {
            System.err.println(e);
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            System.err.println(e);
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            System.err.println(e);
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, e);
        }

        this.anexo = new Anexo();

    }

    public void removerAnexo(Anexo anexo) {
        this.protestoVisualizar.setInstrumentoEletronico(null);
        if (anexo != null && anexo.getId() != null) {
            this.anexoRemocao = anexo;
        }
    }

    public BigDecimal obterCobrancaTotalPagamento() {
        Protocolo protocoloApontamento = null;
        for (Protocolo protocolo : this.protocolos) {
            if (protocolo.getDescricao() != null && protocolo.getDescricao().equals("Apontamento")) {
                protocoloApontamento = protocolo;
                break;
            }
        }

        BigDecimal total = BigDecimal.ZERO;

        if (protestoVisualizar.getTaxaCra() != null) {
            total = total.add(protestoVisualizar.getTaxaCra());
        }
        
        if (protestoVisualizar.getValorAProtestar() != null) {
            total = total.add(protestoVisualizar.getValorAProtestar());
        }
        
        if (protocoloApontamento != null) {
            total = total.add(protocoloApontamento.getValorCartorioComDesconto());
        }
        
        if (protestoVisualizar.getTaxaConselho() != null) {
            total = total.add(protestoVisualizar.getTaxaConselho());
        }
        
        return total;
    }

    public void criarEncerramentoDiarioDistribuicao() {
        String assinatura = null;
        if (usuarioEncerramento != null) {
            assinatura = usuarioEncerramento.getNome() + " - " + usuarioEncerramento.getCargo();
        }

        if (dataFim == null) {
            itemLivroProtocoloServico.criarEncerramentoDiarioDistribuicaoProtesto(livroApontamento, dataInicio, assinatura);
            livroServico.obterFolhaLivro(livroApontamento, configuracao.getRelatorioDistribuicao().getQuantidadeTitulosPorPagina());
        } else if (dataFim != null && dataInicio != null) {
            Date dataAux = dataFim;
            Calendar calendar = new GregorianCalendar();
            while (dataInicio.before(dataAux) || dataInicio.equals(dataAux)) {
                List<ItemLivroProtocolo> result = itemLivroProtocoloServico.buscarEncerramentos(livroApontamento, dataAux, dataAux);
                calendar.setTime(dataAux);
                int diaDaSemana = calendar.get(Calendar.DAY_OF_WEEK);
                if (Utils.isEmpty(result) && diaDaSemana != 1 && diaDaSemana != 7) {
                    itemLivroProtocoloServico.criarEncerramentoDiarioDistribuicaoProtesto(livroApontamento, dataAux, assinatura);
                    livroServico.obterFolhaLivro(livroApontamento, configuracao.getRelatorioDistribuicao().getQuantidadeTitulosPorPagina());
                }
                dataAux = DateUtils.alterDate(dataAux, -1);
            }
        }
        buscarEncerramentos();
        RequestContext.getCurrentInstance().execute("printButton.jq.click();");
    }

    public void criarEncerramentoDiario() {
        String assinatura = null;
        if (usuarioEncerramento != null) {
            assinatura = usuarioEncerramento.getNome() + " - " + usuarioEncerramento.getCargo();
        }

        if (dataFim == null) {
            itemLivroProtocoloServico.criarEncerramentoDiarioApontamentoProtesto(livroApontamento, dataInicio, assinatura);
            Integer quantidadePorFolha = configuracaoSistemaServico.obterConfiguracao().getRelatorioApontamento().getQuantidadeTitulosPorPagina();
            Integer proximoRegistroLivro = livroApontamento.getUltimaFolhaUtilizada() + 1;
            BigDecimal folhaEquivalente = new BigDecimal(proximoRegistroLivro).divide(new BigDecimal(quantidadePorFolha), BigDecimal.ROUND_UP);
            String folha = "";
            if (folhaEquivalente.doubleValue() % 2 == 0) {
                folha = (folhaEquivalente.divide(BigDecimal.valueOf(2))) + "V";
            } else {
                if (!folhaEquivalente.equals(BigDecimal.ONE)) {
                    folha = folhaEquivalente.divide(BigDecimal.valueOf(2), BigDecimal.ROUND_UP).toString();
                } else {
                    folha = "1";
                }
            }
            // Abrir novo livro
            livroApontamento.setUltimaFolhaUtilizada(proximoRegistroLivro);
            if (folhaEquivalente.divide(BigDecimal.valueOf(2)).compareTo(BigDecimal.valueOf(livroApontamento.getFolhas())) > 0) {
                livroApontamento.setFechado(true);
                Livro novoLivro = new Livro();
                novoLivro.setDataAbertura(new Date());
                novoLivro.setTitulo("Livro de apontamento de protesto ");
                novoLivro.setFolhas(livroApontamento.getFolhas());
                novoLivro.setLivroApontamento(true);
                novoLivro.setTipoLivro(livroApontamento.getTipoLivro());
                novoLivro.setNumero(String.valueOf(Integer.parseInt(livroApontamento.getNumero()) + 1));
                novoLivro.setUltimaFolhaUtilizada(0);
                novoLivro.setActive(true);
                livroServico.salvar(novoLivro);
            }
            livroServico.update(livroApontamento);
        } else if (dataFim != null && dataInicio != null) {
            Date dataAux = dataFim;
            Calendar calendar = new GregorianCalendar();
            while (dataInicio.before(dataAux) || dataInicio.equals(dataAux)) {
                List<ItemLivroProtocolo> result = itemLivroProtocoloServico.buscarEncerramentos(livroApontamento, dataAux, dataAux);
                calendar.setTime(dataAux);
                int diaDaSemana = calendar.get(Calendar.DAY_OF_WEEK);
                if (Utils.isEmpty(result) && diaDaSemana != 1 && diaDaSemana != 7) {
                    itemLivroProtocoloServico.criarEncerramentoDiarioApontamentoProtesto(livroApontamento, dataAux, assinatura);
                }
                dataAux = DateUtils.alterDate(dataAux, -1);
            }
        }
        buscarEncerramentos();
        RequestContext.getCurrentInstance().execute("printButton.jq.click();");
    }

    public void cancelarSelagemSelosPostergados() {

        try {

            for (Selo selo : this.protestoVisualizar.getSelosPostergado()) {

                seloServico.atualizarSeloBanco(selo);
                selo = seloServico.pesquisar(selo.getId());
                selo.getLogHistory().add(new LogHistory(usuarioLogado.getLogin(), "Selagem cancelada, motivo : " + this.motivoCancelamentoSelosPostergados, "Selo"));
                selo.setProtesto(null);

                seloServico.update(selo);
            }

            if (this.protestoVisualizar != null) {
                this.protestoVisualizar.setSelosPostergado(null);
                protestoServico.update(this.protestoVisualizar);
            }

            Mensagem.messagemInfoRedirect(Mensagem.SuccessFull, "visualizarProtesto.xhtml?protestoId=" + this.protestoVisualizar.getId());
        } catch (Exception e) {
            System.err.println(e);
            Mensagem.messagemError(Mensagem.Failure);
        }
    }

    public boolean renderizarCancelamentoSelosPostergados() {
        return !Utils.isEmpty(this.protestoVisualizar.getSelosPostergado()) && this.protestoVisualizar.getSelosPostergado().get(0).getProtocoloItem() == null;
    }

    public List<Usuario> autocompletarUsuario(String query) {
        return usuarioServico.autocompletar(query);
    }

    public String formatarDataEncerramento() {
        if (dataInicio == null) {
            return "";
        }
        String retorno = "de " + DateUtils.format(dataInicio);
        if (dataFim != null) {

            retorno += " à " + DateUtils.format(dataFim);
        }
        return retorno;
    }

    public List<Livro> autocompletarLivro(String query) {
        return livroServico.autocompletarLivroApontamento(query);
    }

    public List<Livro> autocompletarLivroProtesto(String query) {
        return livroServico.autocompletarLivroProtesto(query);
    }

    public List<Livro> autocompletarLivroDistribuicao(String query) {
        return livroServico.autocompletarLivroDistribuicao(query);
    }

    public void setProtesto(Protesto protesto) {
        this.protestoPesquisa = protesto;
    }

    public List<Protesto> getProtestos() {
        return protestos;
    }

    public void setProtestos(List<Protesto> protestos) {
        this.protestos = protestos;
    }

    public Protesto getProtestoPesquisa() {
        return protestoPesquisa;
    }

    public void setProtestoPesquisa(Protesto protestoPesquisa) {
        this.protestoPesquisa = protestoPesquisa;
    }

    public Protesto getProtestoDeletar() {
        return protestoDeletar;
    }

    public void setProtestoDeletar(Protesto protestoDeletar) {
        this.protestoDeletar = protestoDeletar;
    }

    public Protesto getProtestoVisualizar() {
        return protestoVisualizar;
    }

    public void setProtestoVisualizar(Protesto protestoVisualizar) {
        this.protestoVisualizar = protestoVisualizar;
    }

    public Protesto getProtestoEditar() {
        return protestoEditar;
    }

    public void setProtestoEditar(Protesto protestoEditar) {
        this.protestoEditar = protestoEditar;
    }

    public List<SituacaoProtesto> getSituacoesProtesto() {
        return situacoesProtesto;
    }

    public void setSituacoesProtesto(List<SituacaoProtesto> situacoesProtesto) {
        this.situacoesProtesto = situacoesProtesto;
    }

    public Pessoa getApresentante() {
        return apresentante;
    }

    public void setApresentante(Pessoa apresentante) {
        this.apresentante = apresentante;
    }

    public List<Pessoa> getApresentantes() {
        return apresentantes;
    }

    public void setApresentantes(List<Pessoa> apresentantes) {
        this.apresentantes = apresentantes;
    }

    public List<Pessoa> getDevedores() {
        return devedores;
    }

    public void setDevedores(List<Pessoa> devedores) {
        this.devedores = devedores;
    }

    public List<Pessoa> getFiadores() {
        return fiadores;
    }

    public void setFiadores(List<Pessoa> fiadores) {
        this.fiadores = fiadores;
    }

    public List<Pessoa> getPortadores() {
        return portadores;
    }

    public void setPortadores(List<Pessoa> portadores) {
        this.portadores = portadores;
    }

    public ProtestoServico getProtestoServico() {
        return protestoServico;
    }

    public void setProtestoServico(ProtestoServico protestoServico) {
        this.protestoServico = protestoServico;
    }

    public SituacaoProtestoServico getSituacaoProtestoServico() {
        return situacaoProtestoServico;
    }

    public void setSituacaoProtestoServico(SituacaoProtestoServico situacaoProtestoServico) {
        this.situacaoProtestoServico = situacaoProtestoServico;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getArquivoId() {
        return arquivoId;
    }

    public void setArquivoId(String arquivoId) {
        this.arquivoId = arquivoId;
    }

    public ProtocoloServico getProtocoloServico() {
        return protocoloServico;
    }

    public void setProtocoloServico(ProtocoloServico protocoloServico) {
        this.protocoloServico = protocoloServico;
    }

    public Cartorio getCartorio() {
        return cartorio;
    }

    public void setCartorio(Cartorio cartorio) {
        this.cartorio = cartorio;
    }

    public Livro getLivroApontamento() {
        return livroApontamento;
    }

    public void setLivroApontamento(Livro livroApontamento) {
        this.livroApontamento = livroApontamento;
    }

    @Override
    public UserSystem getUserSystem() {
        return usuarioServico.getUsuarioLogado();
    }

    public Protocolo getProtocoloApontamentoTitulo() {
        return protocoloApontamentoTitulo;
    }

    public void setProtocoloApontamentoTitulo(Protocolo protocoloApontamentoTitulo) {
        this.protocoloApontamentoTitulo = protocoloApontamentoTitulo;
    }

    public Protocolo getProtocoloRetirada() {
        return protocoloRetirada;
    }

    public void setProtocoloRetirada(Protocolo protocoloRetirada) {
        this.protocoloRetirada = protocoloRetirada;
    }

    public String getCodigoApontamento() {
        return codigoApontamento;
    }

    public void setCodigoApontamento(String codigoApontamento) {
        this.codigoApontamento = codigoApontamento;
    }

    public String getCodigosApontamentos() {
        return codigosApontamentos;
    }

    public void setCodigosApontamentos(String codigosApontamentos) {
        this.codigosApontamentos = codigosApontamentos;
    }

    public List<Protesto> getProtestosEmitirIntimacao() {
        return protestosEmitirIntimacao;
    }

    public void setProtestosEmitirIntimacao(List<Protesto> protestosEmitirIntimacao) {
        this.protestosEmitirIntimacao = protestosEmitirIntimacao;
    }

    public List<Protesto> getProtestosNotificados() {
        return protestosNotificados;
    }

    public void setProtestosNotificados(List<Protesto> protestosNotificados) {
        this.protestosNotificados = protestosNotificados;
    }

    public BigDecimal getTotalProcesso() {
        return totalProcesso;
    }

    public void setTotalProcesso(BigDecimal totalProcesso) {
        this.totalProcesso = totalProcesso;
    }

    public Pessoa getDevedor() {
        return devedor;
    }

    public void setDevedor(Pessoa devedor) {
        this.devedor = devedor;
    }

    public long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(long protocoloId) {
        this.protocoloId = protocoloId;
    }

    public TipoRelatorioProtesto getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(TipoRelatorioProtesto tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public Long getNumeroRemessa() {
        return numeroRemessa;
    }

    public void setNumeroRemessa(Long numeroRemessa) {
        this.numeroRemessa = numeroRemessa;
    }

    public Long getNumeroUltimaRemessa() {
        return numeroUltimaRemessa;
    }

    public void setNumeroUltimaRemessa(Long numeroUltimaRemessa) {
        this.numeroUltimaRemessa = numeroUltimaRemessa;
    }

    public Date getDataPrazo() {
        return dataPrazo;
    }

    public void setDataPrazo(Date dataPrazo) {
        this.dataPrazo = dataPrazo;
    }

    public Date getDataPrazoMin() {
        return dataPrazoMin;
    }

    public void setDataPrazoMin(Date dataPrazoMin) {
        this.dataPrazoMin = dataPrazoMin;
    }

    public Long getNumeroApontamentoInicial() {
        return numeroApontamentoInicial;
    }

    public void setNumeroApontamentoInicial(Long numeroApontamentoInicial) {
        this.numeroApontamentoInicial = numeroApontamentoInicial;
    }

    public Date getDataAceite() {
        return dataAceite;
    }

    public void setDataAceite(Date dataAceite) {
        this.dataAceite = dataAceite;
    }

    public List<Protocolo> getProtocolos() {
        return protocolos;
    }

    public void setProtocolos(List<Protocolo> protocolos) {
        this.protocolos = protocolos;
    }

    public String obterURLPagamento(Protocolo protocolo) {
        return "pagamentoProtocolo.xhtml?protocoloId=" + protocolo.getId() + "&protestoId=" + protestoVisualizar.getId();
    }

    public String obterURLRecibo(Protocolo protocolo) {
        return "visualizarProtesto.xhtml?protocoloId=" + protocolo.getId() + "&protestoId=" + protestoVisualizar.getId();
    }

    public boolean isViaLivro() {
        return viaLivro;
    }

    public void setViaLivro(boolean viaLivro) {
        this.viaLivro = viaLivro;
    }

    public Long getProcesso() {
        return processo;
    }

    public void setProcesso(Long processo) {
        this.processo = processo;
    }

    public TipoBaixaProtesto getTipoBaixaProtesto() {
        return tipoBaixaProtesto;
    }

    public void setTipoBaixaProtesto(TipoBaixaProtesto tipoBaixaProtesto) {
        this.tipoBaixaProtesto = tipoBaixaProtesto;
    }

    public IrregularidadeProtesto getIrregularidade() {
        return irregularidade;
    }

    public void setIrregularidade(IrregularidadeProtesto irregularidade) {
        this.irregularidade = irregularidade;
    }

    public List<IrregularidadeProtesto> getIrregularidades() {
        return irregularidades;
    }

    public void setIrregularidades(List<IrregularidadeProtesto> irregularidades) {
        this.irregularidades = irregularidades;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public boolean isAdicionarCustas() {
        return adicionarCustas;
    }

    public void setAdicionarCustas(boolean adicionarCustas) {
        this.adicionarCustas = adicionarCustas;
    }

    public BigDecimal getTotalCustasProtesto() {
        return totalCustasProtesto;
    }

    public void setTotalCustasProtesto(BigDecimal totalCustasProtesto) {
        this.totalCustasProtesto = totalCustasProtesto;
    }

    public BigDecimal getValorEdital() {
        return valorEdital;
    }

    public void setValorEdital(BigDecimal valorEdital) {
        this.valorEdital = valorEdital;
    }

    public String getTipoEmissaoCartas() {
        return tipoEmissaoCartas;
    }

    public void setTipoEmissaoCartas(String tipoEmissaoCartas) {
        this.tipoEmissaoCartas = tipoEmissaoCartas;
    }

    public Livro getLivroProtesto() {
        return livroProtesto;
    }

    public void setLivroProtesto(Livro livroProtesto) {
        this.livroProtesto = livroProtesto;
    }

    public Integer getTitulosPesquisa() {
        return titulosPesquisa;
    }

    public void setTitulosPesquisa(Integer titulosPesquisa) {
        this.titulosPesquisa = titulosPesquisa;
    }

    public CustaProtesto getCustaProtesto() {
        return custaProtesto;
    }

    public void setCustaProtesto(CustaProtesto custaProtesto) {
        this.custaProtesto = custaProtesto;
    }

    public CustaProtesto getCustaRemocao() {
        return custaRemocao;
    }

    public void setCustaRemocao(CustaProtesto custaRemocao) {
        this.custaRemocao = custaRemocao;
    }

    public String getTituloSerasa() {
        return tituloSerasa;
    }

    public void setTituloSerasa(String tituloSerasa) {
        this.tituloSerasa = tituloSerasa;
    }

    public boolean isExibirDialogSelosPostergado() {
        return exibirDialogSelosPostergado;
    }

    public void setExibirDialogSelosPostergado(boolean exibirDialogSelosPostergado) {
        this.exibirDialogSelosPostergado = exibirDialogSelosPostergado;
    }

    public boolean isEmitirInstrumentosAnexados() {
        return emitirInstrumentosAnexados;
    }

    public void setEmitirInstrumentosAnexados(boolean emitirInstrumentosAnexados) {
        this.emitirInstrumentosAnexados = emitirInstrumentosAnexados;
    }

    public boolean isCargaInicial() {
        return cargaInicial;
    }

    public void setCargaInicial(boolean cargaInicial) {
        this.cargaInicial = cargaInicial;
    }

    public Long getTipoRelatorioProtestados() {
        return tipoRelatorioProtestados;
    }

    public void setTipoRelatorioProtestados(Long tipoRelatorioProtestados) {
        this.tipoRelatorioProtestados = tipoRelatorioProtestados;
    }

    public boolean isExibirDialogSelos() {
        return exibirDialogSelos;
    }

    public void setExibirDialogSelos(boolean exibirDialogSelos) {
        this.exibirDialogSelos = exibirDialogSelos;
    }

    public List<Selo> getSelosExibicao() {
        return selosExibicao;
    }

    public void setSelosExibicao(List<Selo> selosExibicao) {
        this.selosExibicao = selosExibicao;
    }

    public String getOrigemSelos() {
        return origemSelos;
    }

    public void setOrigemSelos(String origemSelos) {
        this.origemSelos = origemSelos;
    }

    public List<ItemLivroProtocolo> getEncerramentos() {
        return encerramentos;
    }

    public void setEncerramentos(List<ItemLivroProtocolo> encerramentos) {
        this.encerramentos = encerramentos;
    }

    public ItemLivroProtocolo getEncerramento() {
        return encerramento;
    }

    public void setEncerramento(ItemLivroProtocolo encerramento) {
        this.encerramento = encerramento;
    }

    public ItemLivroProtocolo getEncerramentoDelete() {
        return encerramentoDelete;
    }

    public void setEncerramentoDelete(ItemLivroProtocolo encerramentoDelete) {
        this.encerramentoDelete = encerramentoDelete;
    }

    public Usuario getUsuarioEncerramento() {
        return usuarioEncerramento;
    }

    public void setUsuarioEncerramento(Usuario usuarioEncerramento) {
        this.usuarioEncerramento = usuarioEncerramento;
    }

    public boolean isOrdemProtesto() {
        return ordemProtesto;
    }

    public void setOrdemProtesto(boolean ordemProtesto) {
        this.ordemProtesto = ordemProtesto;
    }

    public Usuario getUsuarioIrregularidade() {
        return usuarioIrregularidade;
    }

    public void setUsuarioIrregularidade(Usuario usuarioIrregularidade) {
        this.usuarioIrregularidade = usuarioIrregularidade;
    }

    public Pessoa getDevedorCorrecao() {
        return devedorCorrecao;
    }

    public void setDevedorCorrecao(Pessoa devedorCorrecao) {
        this.devedorCorrecao = devedorCorrecao;
    }

    public Usuario getUsuarioNotificacao() {
        return usuarioNotificacao;
    }

    public void setUsuarioNotificacao(Usuario usuarioNotificacao) {
        this.usuarioNotificacao = usuarioNotificacao;
    }

    public List<Protesto> getTitulosParaEdital() {
        return titulosParaEdital;
    }

    public void setTitulosParaEdital(List<Protesto> titulosParaEdital) {
        this.titulosParaEdital = titulosParaEdital;
    }

    public List<Protesto> getTitulosSelecionados() {
        return titulosSelecionados;
    }

    public void setTitulosSelecionados(List<Protesto> titulosSelecionados) {
        this.titulosSelecionados = titulosSelecionados;
    }

    public boolean isAdicionarTitulosMarcados() {
        return adicionarTitulosMarcados;
    }

    public void setAdicionarTitulosMarcados(boolean adicionarTitulosMarcados) {
        this.adicionarTitulosMarcados = adicionarTitulosMarcados;
    }

    public List<AtoGenerico> getEditaisPublicados() {
        return editaisPublicados;
    }

    public void setEditaisPublicados(List<AtoGenerico> editaisPublicados) {
        this.editaisPublicados = editaisPublicados;
    }

    public List<ControlePagamentoProtesto> getControlePagamentosProtesto() {
        return controlePagamentosProtesto;
    }

    public void setControlePagamentosProtesto(List<ControlePagamentoProtesto> controlePagamentosProtesto) {
        this.controlePagamentosProtesto = controlePagamentosProtesto;
    }

    public ControlePagamentoProtesto getControlePagamentoProtesto() {
        return controlePagamentoProtesto;
    }

    public void setControlePagamentoProtesto(ControlePagamentoProtesto controlePagamentoProtesto) {
        this.controlePagamentoProtesto = controlePagamentoProtesto;
    }

    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
    }

    public Anexo getAnexo() {
        return anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }

    public String getMaterializarPor() {
        return materializarPor;
    }

    public void setMaterializarPor(String materializarPor) {
        this.materializarPor = materializarPor;
    }

    public Boolean getEditalPesquisa() {
        return editalPesquisa;
    }

    public void setEditalPesquisa(Boolean editalPesquisa) {
        this.editalPesquisa = editalPesquisa;
    }

    public MotivoEditalProtesto getMotivoEditalProtesto() {
        return motivoEditalProtesto;
    }

    public void setMotivoEditalProtesto(MotivoEditalProtesto motivoEditalProtesto) {
        this.motivoEditalProtesto = motivoEditalProtesto;
    }

    public Usuario getUsuarioEdital() {
        return usuarioEdital;
    }

    public void setUsuarioEdital(Usuario usuarioEdital) {
        this.usuarioEdital = usuarioEdital;
    }

    public TipoDeConsultaDeTitulos getTipoDeConsultaDeTitulos() {
        return tipoDeConsultaDeTitulos;
    }

    public void setTipoDeConsultaDeTitulos(TipoDeConsultaDeTitulos tipoDeConsultaDeTitulos) {
        this.tipoDeConsultaDeTitulos = tipoDeConsultaDeTitulos;
    }

    public ConfiguracaoSistema getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(ConfiguracaoSistema configuracao) {
        this.configuracao = configuracao;
    }

    public Boolean getPostergadoPesquisa() {
        return postergadoPesquisa;
    }

    public void setPostergadoPesquisa(Boolean postergadoPesquisa) {
        this.postergadoPesquisa = postergadoPesquisa;
    }

    public List<Protesto> getProtestosFimFalimentar() {
        return protestosFimFalimentar;
    }

    public void setProtestosFimFalimentar(List<Protesto> protestosFimFalimentar) {
        this.protestosFimFalimentar = protestosFimFalimentar;
    }

    public List<Anexo> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }

    public boolean isEditalValorFixo() {
        return editalValorFixo;
    }

    public void setEditalValorFixo(boolean editalValorFixo) {
        this.editalValorFixo = editalValorFixo;
    }

    public boolean isMarcarEdital() {
        return marcarEdital;
    }

    public void setMarcarEdital(boolean marcarEdital) {
        this.marcarEdital = marcarEdital;
    }

    public boolean isMarcarDevolucao() {
        return marcarDevolucao;
    }

    public void setMarcarDevolucao(boolean marcarDevolucao) {
        this.marcarDevolucao = marcarDevolucao;
    }

    public IrregularidadeProtesto getIrregularidadeProtesto() {
        return irregularidadeProtesto;
    }

    public void setIrregularidadeProtesto(IrregularidadeProtesto irregularidadeProtesto) {
        this.irregularidadeProtesto = irregularidadeProtesto;
    }

    public Usuario getUsuarioAssinatura() {
        return usuarioAssinatura;
    }

    public void setUsuarioAssinatura(Usuario usuarioAssinatura) {
        this.usuarioAssinatura = usuarioAssinatura;
    }

    public SituacaoProtesto getSpRemocao() {
        return spRemocao;
    }

    public void setSpRemocao(SituacaoProtesto spRemocao) {
        this.spRemocao = spRemocao;
    }

    public SituacaoProtesto getSpEditar() {
        return spEditar;
    }

    public void setSpEditar(SituacaoProtesto spEditar) {
        this.spEditar = spEditar;
    }

    public Protocolo getProtocoloExibicao() {
        return protocoloExibicao;
    }

    public void setProtocoloExibicao(Protocolo protocoloExibicao) {
        this.protocoloExibicao = protocoloExibicao;
    }

    public List<Imposto> getImpostos() {
        return impostos;
    }

    public void setImpostos(List<Imposto> impostos) {
        this.impostos = impostos;
    }

    public boolean isAdicionarTaxaBoleto() {
        return adicionarTaxaBoleto;
    }

    public void setAdicionarTaxaBoleto(boolean adicionarTaxaBoleto) {
        this.adicionarTaxaBoleto = adicionarTaxaBoleto;
    }

    public Ato getTaxaBoleto() {
        return taxaBoleto;
    }

    public void setTaxaBoleto(Ato taxaBoleto) {
        this.taxaBoleto = taxaBoleto;
    }

    public ArrayList<Protesto> getBoletoProtestoASerCancelado() {
        return boletoProtestoASerCancelado;
    }

    public void setBoletoProtestoASerCancelado(ArrayList<Protesto> boletoProtestoASerCancelado) {
        this.boletoProtestoASerCancelado = boletoProtestoASerCancelado;
    }

    public Ordenacao getOrdernacao() {
        return ordernacao;
    }

    public void setOrdernacao(Ordenacao ordernacao) {
        this.ordernacao = ordernacao;
    }

    public Ato getAtoDespesasConducao() {
        return atoDespesasConducao;
    }

    public void setAtoDespesasConducao(Ato atoDespesasConducao) {
        this.atoDespesasConducao = atoDespesasConducao;
    }

    public TipoSituacaoProtesto getTipoSituacaoProtesto() {
        return tipoSituacaoProtesto;
    }

    public void setTipoSituacaoProtesto(TipoSituacaoProtesto tipoSituacaoProtesto) {
        this.tipoSituacaoProtesto = tipoSituacaoProtesto;
    }

    private void adicionarCustasBoletoEAlterarPrazo(Protesto protesto) {
        if (adicionarTaxaBoleto && taxaBoleto != null) {
            CustaProtesto custaProtesto = new CustaProtesto();
            custaProtesto.setAto(taxaBoleto);
            custaProtesto.setDataCadastro(new Date());
            custaProtesto.setDescricao("Taxa de boleto");
            custaProtesto.setUsuario(usuarioLogado);
            custaProtesto.setValor(valorTaxaBoleto);
            protesto.getCustasProtesto().add(custaProtesto);
        }

        if (protesto.getDataPrazoEdital() != null) {
            protesto.setDataPrazoEdital(dataPrazo);
        } else {
            protesto.setDataPrazoProtesto(dataPrazo);
        }

        SituacaoProtesto situacaoProtesto = new SituacaoProtesto();
        situacaoProtesto.setDataCriacao(new Date());
        situacaoProtesto.setTipoSituacaoProtesto(TipoSituacaoProtesto.BOLETO_REGERADO);
        situacaoProtesto.setObservacao("Boleto do título regerado. Vencimento do boleto : " + DateUtils.format(protesto.getDataPrazoEdital() != null ? protesto.getDataPrazoEdital() : protesto.getDataPrazoProtesto()));
        situacaoProtesto.setUsuario(usuarioLogado);
        situacaoProtesto.setProtesto(protesto);

        situacaoProtestoServico.salvar(situacaoProtesto);
        protestoServico.update(protesto);
    }

    public BigDecimal getValorTaxaBoleto() {
        return valorTaxaBoleto;
    }

    public void setValorTaxaBoleto(BigDecimal valorTaxaBoleto) {
        this.valorTaxaBoleto = valorTaxaBoleto;
    }

    public boolean isUtilizarProcessoApontamento() {
        return utilizarProcessoApontamento;
    }

    public void setUtilizarProcessoApontamento(boolean utilizarProcessoApontamento) {
        this.utilizarProcessoApontamento = utilizarProcessoApontamento;
    }

    public boolean isUtilizarProcessoRetirada() {
        return utilizarProcessoRetirada;
    }

    public void setUtilizarProcessoRetirada(boolean utilizarProcessoRetirada) {
        this.utilizarProcessoRetirada = utilizarProcessoRetirada;
    }

    public boolean isUtilizarProcessoDevolucao() {
        return utilizarProcessoDevolucao;
    }

    public void setUtilizarProcessoDevolucao(boolean utilizarProcessoDevolucao) {
        this.utilizarProcessoDevolucao = utilizarProcessoDevolucao;
    }

    public boolean isUtilizarProcessoProtesto() {
        return utilizarProcessoProtesto;
    }

    public void setUtilizarProcessoProtesto(boolean utilizarProcessoProtesto) {
        this.utilizarProcessoProtesto = utilizarProcessoProtesto;
    }

    public boolean isUtilizarProcessoBaixa() {
        return utilizarProcessoBaixa;
    }

    public void setUtilizarProcessoBaixa(boolean utilizarProcessoBaixa) {
        this.utilizarProcessoBaixa = utilizarProcessoBaixa;
    }

    public String getMotivoCancelamentoSelosPostergados() {
        return motivoCancelamentoSelosPostergados;
    }

    public void setMotivoCancelamentoSelosPostergados(String motivoCancelamentoSelosPostergados) {
        this.motivoCancelamentoSelosPostergados = motivoCancelamentoSelosPostergados;
    }

    public ModeloDocumento getModeloDocumentoEdital() {
        return modeloDocumentoEdital;
    }

    public void setModeloDocumentoEdital(ModeloDocumento modeloDocumentoEdital) {
        this.modeloDocumentoEdital = modeloDocumentoEdital;
    }

    public StatusProtesto getStatusBaixaTituloDistribuicao() {
        return statusBaixaTituloDistribuicao;
    }

    public void setStatusBaixaTituloDistribuicao(StatusProtesto statusBaixaTituloDistribuicao) {
        this.statusBaixaTituloDistribuicao = statusBaixaTituloDistribuicao;
    }

    public Integer getCentral() {
        return central;
    }

    public void setCentral(Integer central) {
        this.central = central;
    }

    public Long getTipoRelatorioBaixados() {
        return tipoRelatorioBaixados;
    }

    public void setTipoRelatorioBaixados(Long tipoRelatorioBaixados) {
        this.tipoRelatorioBaixados = tipoRelatorioBaixados;
    }

    public Usuario getUsuarioQueAssina() {
        return usuarioQueAssina;
    }

    public void setUsuarioQueAssina(Usuario usuarioQueAssina) {
        this.usuarioQueAssina = usuarioQueAssina;
    }

    public Usuario getUsuarioQueEscreveu() {
        return usuarioQueEscreveu;
    }

    public void setUsuarioQueEscreveu(Usuario usuarioQueEscreveu) {
        this.usuarioQueEscreveu = usuarioQueEscreveu;
    }

    public boolean isProvimentoIntimacaoProv97() {
        return provimentoIntimacaoProv97;
    }

    public void setProvimentoIntimacaoProv97(boolean provimentoIntimacaoProv97) {
        this.provimentoIntimacaoProv97 = provimentoIntimacaoProv97;
    }

    public void vasculharDados() {

        ConfiguracaoSistema config = configuracaoSistemaServico.obterConfiguracao();

        for (Protesto protesto : protestos) {
            for (Pessoa pessoa : protesto.getDevedores()) {
                pessoa = pessoaServico.vasculharDados(pessoa);

                if (pessoa.getEmail() == null || pessoa.getTelefones() == null) {
                    pessoa = pessoaServico.getDadosCentral(pessoa, config);
                }
            }

        }
    }

    public void vasculharDadosCentral() {
        ConfiguracaoSistema config = configuracaoSistemaServico.obterConfiguracao();

        for (Protesto protesto : protestos) {
            for (Pessoa pessoa : protesto.getDevedores()) {
                if (pessoa.getEmail() == null || pessoa.getTelefones() == null) {

                    pessoa = pessoaServico.getDadosCentral(pessoa, config);
                }
            }
        }
    }

    public String ultimaSituacao(Protesto protesto) {

        SituacaoProtesto situacao = situacaoProtestoServico.ultimaSituacaoProtesto(protesto);

        if (situacao != null) {
            return (situacao.getSituacaoEnvioProtesto() != null ? (situacao.getSituacaoEnvioProtesto().getNome() + " - ") : "") + situacao.getObservacao();
        } else {
            return "Nenhuma situação encontrada";
        }
    }

    public Telefone getTelefoneDevedor() {
        return telefoneDevedor;
    }

    public void setTelefoneDevedor(Telefone telefoneDevedor) {
        this.telefoneDevedor = telefoneDevedor;
    }

    public boolean isAdicionarTitulosVencidosPendentes() {
        return adicionarTitulosVencidosPendentes;
    }

    public void setAdicionarTitulosVencidosPendentes(boolean adicionarTitulosVencidosPendentes) {
        this.adicionarTitulosVencidosPendentes = adicionarTitulosVencidosPendentes;
    }

    public List<StatusProtesto> getStatusTitulosPedentes() {
        return statusTitulosPedentes;
    }

    public void setStatusTitulosPedentes(List<StatusProtesto> statusTitulosPedentes) {
        this.statusTitulosPedentes = statusTitulosPedentes;
    }

    public EspecieTitulo getEspecieTitulo() {
        return especieTitulo;
    }

    public void setEspecieTitulo(EspecieTitulo especieTitulo) {
        this.especieTitulo = especieTitulo;
    }

    public List<TipoSituacaoProtesto> getTipoSituacaoProtestoRelatorio() {
        return tipoSituacaoProtestoRelatorio;
    }

    public void setTipoSituacaoProtestoRelatorio(List<TipoSituacaoProtesto> tipoSituacaoProtestoRelatorio) {
        this.tipoSituacaoProtestoRelatorio = tipoSituacaoProtestoRelatorio;
    }

    public boolean isFiltrarPorDataDistribuicao() {
        return filtrarPorDataDistribuicao;
    }

    public void setFiltrarPorDataDistribuicao(boolean filtrarPorDataDistribuicao) {
        this.filtrarPorDataDistribuicao = filtrarPorDataDistribuicao;
    }

    public void atribuirDevedorCorrecao(Pessoa pessoa) {
        this.devedorCorrecao = pessoa;

        if (!Utils.isEmpty(this.devedorCorrecao.getTelefones())) {
            telefoneDevedor = this.devedorCorrecao.getTelefones().get(0);
        }
    }

    public Date getDataInicioApontamento() {
        return dataInicioApontamento;
    }

    public void setDataInicioApontamento(Date dataInicioApontamento) {
        this.dataInicioApontamento = dataInicioApontamento;
    }

    public Date getDataFimApontamento() {
        return dataFimApontamento;
    }

    public void setDataFimApontamento(Date dataFimApontamento) {
        this.dataFimApontamento = dataFimApontamento;
    }

    public MotivoSuspensaoProtesto getMotivoSuspensaoProtesto() {
        return motivoSuspensaoProtesto;
    }

    public void setMotivoSuspensaoProtesto(MotivoSuspensaoProtesto motivoSuspensaoProtesto) {
        this.motivoSuspensaoProtesto = motivoSuspensaoProtesto;
    }

    public String getDescricaoMotivoSuspensao() {
        return descricaoMotivoSuspensao;
    }

    public void setDescricaoMotivoSuspensao(String descricaoMotivoSuspensao) {
        this.descricaoMotivoSuspensao = descricaoMotivoSuspensao;
    }

    public TipoSolucaoSuspensaoProtesto getTipoSolucaoSuspensaoProtesto() {
        return tipoSolucaoSuspensaoProtesto;
    }

    public void setTipoSolucaoSuspensaoProtesto(TipoSolucaoSuspensaoProtesto tipoSolucaoSuspensaoProtesto) {
        this.tipoSolucaoSuspensaoProtesto = tipoSolucaoSuspensaoProtesto;
    }

    public String getDescricaoSolucaoSuspensao() {
        return descricaoSolucaoSuspensao;
    }

    public void setDescricaoSolucaoSuspensao(String descricaoSolucaoSuspensao) {
        this.descricaoSolucaoSuspensao = descricaoSolucaoSuspensao;
    }
}
